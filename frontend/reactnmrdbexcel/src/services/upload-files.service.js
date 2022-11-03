import http from "../http-common";
class UploadFilesService {
    upload(filex, filez, name, seq, sname, onUploadProgress) {
        let formData = new FormData();
        formData.append("file", filex);
        formData.append("zip", filez);
        formData.append("seq", seq);
        formData.append("name", name);
        formData.append("sname", sname);

        return http.post("/api/excel/uploadfrag", formData, {
            headers: {
                "Content-Type": "multipart/form-data",
            },
            onUploadProgress,
        });
    }
    getFiles() {
        return http.get("/api/excel/uploadfrag");
    }

    getProteins() {
        return http.get("/api/excel/getproteins");
    }

    getCockByPname(pname) {
        console.log(pname);
        let formData = new FormData();
        formData.append("pname", pname);
        return http.post("/api/excel/findAllCockByProtname", formData);
    }
    getQueryLigandZid(pname) {
        console.log(pname);
        let formData = new FormData();
        formData.append("zid", pname);
        return http.post("/api/excel/findQueryByZid", formData);
    }
    getZipFromDB(pid, dird) {
        let formData = new FormData();
        formData.append("pid", pid);
        formData.append("dir", dird);

        return http.post("/api/excel/getZipFromDB", formData, {
            headers: {
                'Content-Type': 'application/json; application/octet-stream'

            },responseType: 'blob'
        });
    }
}
export default new UploadFilesService();
