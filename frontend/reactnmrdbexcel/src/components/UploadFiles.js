import React, { Component } from "react";
import UploadService from "../services/upload-files.service";
import FormControl from '@mui/material/FormControl';
import CircularProgressWithLabel from "../mui/CircularProgressWithLabel"

import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import TextField from '@mui/material/TextField';
import Link from '@mui/material/Link';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import {Button, InputLabel, Select} from "@mui/material";
import Typography from '@mui/material/Typography';
import AppBar from '@mui/material/AppBar';
import Toolbar from '@mui/material/Toolbar';
import { withStyles } from '@mui/styles';
import FileSaver from "file-saver";


const style = (theme) =>({
    typography: {
        subtitle1: {
            fontSize: 12,
        }}

});
class UploadFiles extends Component {
    constructor(props) {
        super(props);
        this.selectFilex = this.selectFilex.bind(this);
        this.selectFilez = this.selectFilez.bind(this);
        this.selectName = this.selectName.bind(this);
        this.selectSName = this.selectSName.bind(this);
        this.selectSeq = this.selectSeq.bind(this);
        this.upload = this.upload.bind(this);
        this.getCockByPname = this.getCockByPname.bind(this);
        this.getQueryLigandZid = this.getQueryLigandZid.bind(this);
        this.handleChangeq1 = this.handleChangeq1.bind(this);
        this.handleChangeq2 = this.handleChangeq2.bind(this);
        this.saveFile = this.saveFile.bind(this)
        this.dowloadZip = this.dowloadZip.bind(this);
        this.updateProteinsList = this.updateProteinsList.bind(this);
        this.handleexplist = this.handleexplist.bind(this);
        this.pressZid = this.pressZid.bind(this);

        this.state = {
            selectedFilesx: undefined,
            selectedFilesz: undefined,
            name: "pro1",
            seq: "SLREVRTIKVFTTVDNINLHTQVVDMSMTYGQQFGPTYLDGADVTKIKPHNSHEGKTFYVLPNDDTLRVEAFEYYHTTDPSFLGRYMSALNHTKKWKYPQVNGLTSIKWADNNCYLATALLTLQQIELKFNPPALQDAYYRARAGEAANFCALILAYCNKTVGELGDVRETMSYLFQHANLDSCKRVLNVVCKTCGQQQT",
            sname: "screepro1",
            currentFile: undefined,
            progress: 0,
            message: "",
            fileInfos: [],
            query1: "",
            query1res: [],
            proteins: [],
            errorsname: false,
            errorpname: false,
            errorseq: false,
            query2: "",
            query2res: [],
        };
    }
    componentDidMount() {
         this.updateProteinsList();
    }
    componentDidUpdate(prevProps, prevState, snapshot) {
       // this.updateProteinsList();
    }

    handleexplist(event) {
        // const { options } = event.target;
        // const value = [];
        // for (let i = 0, l = options.length; i < l; i += 1) {
        //     if (options[i].selected) {
        //         value.push(options[i].value);
        //     }
        // }
         console.log("Events", event);
    }
    updateProteinsList() {
        UploadService.getProteins().then((response) => {
            this.setState({
                proteins: response.data,
            });
        });
    }
    selectFilex(event) {
        this.setState({
            selectedFilesx: event.target.files,
        });
    }
    selectName(event) {
        let pname = this.state.proteins.map(a => a.name);
        if(pname.includes(event.target.value))
        {
            this.setState(
            {errorpname: true}
            )}
        else{
        this.setState({
           name: event.target.value,
            errorpname: false
        });}
    }
    selectSName(event) {
        console.log("sname ini");
        let name = this.state.proteins.filter(a => {if (a.name === this.state.name) return a.expname});

        let scree = "";
        if( name.length > 0){
            console.log("sname", name[0].expname);
            scree = [...name[0].expname];
        }

        console.log(scree);
        console.log("sname end");
        if(scree.includes(event.target.value)){
            this.setState(
                {errorsname: true})
        }
        else{
            this.setState({
                sname: event.target.value,
                errorsname: false
            });
        }
    }

    selectSeq(event) {
        let rettest = /^[ABCDEFGHIJKLMNOPQRSTUVWYZ*-]+$/.test(event.target.value);
        this.setState({
            seq: event.target.value,
            errorseq: !rettest
        });
    }
    selectFilez(event) {
        this.setState({
            selectedFilesz: event.target.files,
        });
    }

    upload() {
        let currentFilex = this.state.selectedFilesx[0];
        let currentFilez = this.state.selectedFilesz[0];
        let name = this.state.name;
        let seq = this.state.seq;
        let sname = this.state.sname;

        this.setState({
            progress: 0,
            currentFilez: currentFilez,
        });

        UploadService.upload(currentFilex,currentFilez,name,seq,sname, (event) => {
            this.setState({
                progress: Math.round((100 * event.loaded) / event.total),
            });
        })
            .then((response) => {
                this.setState({
                    message: response.data.message,
                });
                //return UploadService.getFiles();
            })
            .then((files) => {
                //this.setState({
                //    fileInfos: files.data,
                //});
                console.log("run updateProteinsList")
                this.updateProteinsList();

            })
            .catch(() => {
                this.setState({
                    progress: 0,
                    message: "Could not upload the file!",
                    currentFilez: undefined,
                });
            });

        this.setState({
            selectedFiles: undefined,
        });
    }
    getCockByPname(e) {
        console.log(this.state.query1);
        UploadService.getCockByPname(this.state.query1).then((responce) =>{
            console.log(responce.data);
            this.setState({query1res: [...responce.data], query2res: []})
        })
    }
    getQueryLigandZid(e) {
        console.log(this.state.query2);
        UploadService.getQueryLigandZid(this.state.query2).then((responce) =>{
            console.log(responce.data);
            this.setState({query2res: [...responce.data], query1res: []})
        })
    }
    handleChangeq1(event) {
        this.setState({ query1: event.target.value })
    }
    handleChangeq2(event) {
        this.setState({ query2: event.target.value })
    }
   saveFile(blob1) {
      // var blob = new Blob(["Hello, world!"], {
       const byteArray = atob(blob1);
       var blob = new Blob([byteArray], {
           type: "application/zip"
       });
       FileSaver.saveAs(blob, "d1.zip");
    };

    pressZid(event) {
        console.log(event);
        console.log(event.currentTarget.value);
        UploadService.getQueryLigandZid(event.currentTarget.value).then((responce) =>{
            console.log(responce.data);
            this.setState({query2res: [...responce.data], query1res: []})
        })
    }
    dowloadZip(pid, name, dird) {
      console.log(pid);
      UploadService.getZipFromDB(pid, dird).then(
          (e) => {
              FileSaver.saveAs(new Blob([e.data]), name+"_"+dird+".zip");
              //const url = window.URL.createObjectURL(new Blob([e.data]));
              /*const link = document.createElement('a');
              link.href = url;
              link.setAttribute('download', 'file.zip'); //or any other extension
              document.body.appendChild(link);
              link.click();*/
          }
      )

    };

    render() {
        const {
            selectedFiles,
            currentFilez,
            progress,
            message,
            fileInfos,
            proteins
        } = this.state;

        const { classes } = this.props;

        if (this.state.selectedFilesz !== undefined) console.log(this.state.selectedFilesz[0].name);
        console.log("render ini");
        console.log(this.state.query1res);
        console.log("render end");
        console.log(this.state.proteins);
        console.log("proteins end");
        return (
            <div style={{padding: 16, margin: 'auto', maxWidth: "90%"}}>
                <Box sx={{ flexGrow: 1 }}>
                    <Grid container spacing={2}>
                    <AppBar position="static" spacing={2}>
                        <Toolbar>
                            <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
                                Database blueprint for NMR based fragment screening
                            </Typography>

                        </Toolbar>
                    </AppBar>


                        <Grid item xs={3}>
                            <label htmlFor="btn-uploadx">
                                <input
                                    id="btn-uploadx"
                                    name="btn-uploadx"
                                    style={{display: 'none'}}
                                    type="file"
                                    onChange={this.selectFilex}/>
                                <Button
                                    className="btn-choosex"
                                    variant="outlined"
                                    component="span">
                                    Excel File
                                </Button>
                            </label>
                        </Grid>
                        <Grid item xs={3}>
                            <Typography variant="h8">
                                {this.state.selectedFilesx !== undefined ? this.state.selectedFilesx[0].name : "no file Excel"}
                            </Typography>
                        </Grid>

                        <Grid item xs={3}>
                            {/*https://github.com/bezkoder/material-ui-file-upload/blob/master/src/components/upload-files.component.js*/}
                            {/*Zip File
                            <label className="btn btn-default">
                                <input type="file" onChange={this.selectFilez}/>
                            </label>*/}
                            <label htmlFor="btn-uploadz">
                                <input
                                    id="btn-uploadz"
                                    name="btn-uploadz"
                                    style={{display: 'none'}}
                                    type="file"
                                    onChange={this.selectFilez}/>
                                <Button
                                    className="btn-choosez"
                                    variant="outlined"
                                    component="span">
                                    ZIP File
                                </Button>
                            </label>
                        </Grid>
                        <Grid item xs={3}>
                            <Typography variant="h8">
                                {this.state.selectedFilesz !== undefined ? this.state.selectedFilesz[0].name : "no file ZIP"}
                            </Typography>
                        </Grid>

                        <Grid item xs={6}>

                            <TextField
                                label="Screening name"
                                type="text"
                                defaultValue={this.state.sname}
                                onChange={this.selectSName}
                                error={this.state.errorsname}
                                helperText={this.state.errorpname ? 'Screeaning present in DB' : ' '}
                            />
                        </Grid>
                        <Grid item xs={6}>

                            <TextField
                                label="Protein name"
                                defaultValue={this.state.name}
                                onChange={this.selectName}
                                /*error={this.state.errorpname}
                                helperText={this.state.errorpname ? 'Protein present in db' : ' '}*/
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <TextField
                                id="standard-multiline-flexible"
                                label="Protein sequence"
                                multiline
                                maxRows={8}
                                sx={{m: 1, width: '50ch'}}
                                value={this.state.seq}
                                onChange={this.selectSeq}
                                error={this.state.errorseq}
                                variant="standard"
                                helperText={this.state.errorseq ? 'Enter valid fasta sequence' : ' '}
                            />
                        </Grid>
                        <Grid item xs={12}>
                            <Button
                                variant="contained"
                                disabled={!this.state.selectedFilesz || !this.state.selectedFilesx || this.state.errorsname || this.state.errorseq}
                                onClick={this.upload}
                            >
                                Upload files
                            </Button>
                        </Grid>

                        {currentFilez && (
                            <Grid item xs={12}>
                                <div className="progress">

                                    <CircularProgressWithLabel value={progress}/>
                                </div>
                            </Grid>
                        )}
                        <br/>
                        <div className="alert alert-light" role="alert">
                            {message}
                        </div>
                    </Grid>
                </Box>
                {/* <div className="card">
                    <div className="card-header">List of Files</div>
                    <ul className="list-group list-group-flush">
                        {fileInfos &&
                            fileInfos.map((file, index) => (
                                <li className="list-group-item" key={index}>
                                    <a href={file.url}>{file.name}</a>
                                </li>
                            ))}
                    </ul>
                </div>*/}

                {this.state.proteins.length > 0 && (
                    <div>
                        <TableContainer component={Paper}>
                            <Table sx={{minWidth: 650}} aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>ID</TableCell>
                                        <TableCell align="right">Protein name</TableCell>
                                        <TableCell align="right">Experiments</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {this.state.proteins.map((row) => (
                                        <TableRow
                                            key={row.pid}
                                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                        >
                                            <TableCell component="th" scope="row">
                                                {row.pid}
                                            </TableCell>
                                            <TableCell align="right">{row.name}</TableCell>
                                            <TableCell align="right">
                                                <Select
                                                    multiple
                                                    native
                                                    //value={personName}
                                                    // @ts-ignore Typings are not considering `native`
                                                    onChange={this.handleexplist}
                                                    //label="Native"
                                                    autoWidth
                                                >
                                                    {row.expname.map((name) => (
                                                        <option key={name} value={name}>
                                                            {name}
                                                        </option>
                                                    ))}
                                                </Select>
                                            </TableCell>
                                            {/*<TableCell align="right">{row.expname.join("-")}</TableCell>*/}
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>

                    </div>
                )}

                <Box>
                <div>
                   Search protein name
                    <Button onClick={this.getCockByPname}>Submit</Button>
                    <input
                        type="text"
                        value={this.state.query1}
                        onChange={this.handleChangeq1}
                    />
                </div>
                </Box>
                <Box>
                    <div>
                        Query Ligand ZID
                        <Button onClick={this.getQueryLigandZid}>Submit</Button>
                        <input
                            type="text"
                            value={this.state.query2}
                            onChange={this.handleChangeq2}
                        />
                    </div>
                </Box>
                <Box>
                    <Typography>Query Results</Typography>
                </Box>
                {this.state.query2res.length > 0 && (
                    <div>
                        <TableContainer component={Paper}>
                            <Table sx={{minWidth: 650}} aria-label="simple table">
                                <TableHead>
                                    <TableRow>
                                        <TableCell>ID</TableCell>
                                        <TableCell align="right">Protein</TableCell>
                                        <TableCell align="right">Screening</TableCell>
                                        <TableCell align="right">KD</TableCell>
                                        <TableCell align="right">Binding</TableCell>
                                        <TableCell align="right">Residues</TableCell>
                                        <TableCell align="right">T2</TableCell>
                                        <TableCell align="right">CSP</TableCell>
                                        <TableCell align="right">STD</TableCell>
                                        <TableCell align="right">Affinity</TableCell>
                                        <TableCell align="right">Mapping</TableCell>
                                        <TableCell align="right">Smile Formula</TableCell>

                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {this.state.query2res.map((row) => (
                                        <TableRow
                                            key={row.pid}
                                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                        >
                                            <TableCell component="th" scope="row">
                                                {row.pid}
                                            </TableCell>
                                            <TableCell align="right">{row.proteinname}</TableCell>
                                            <TableCell align="right">{row.screeningname}</TableCell>
                                            <TableCell align="right">{row.kd}</TableCell>
                                            <TableCell align="right">{row.binding}</TableCell>
                                            <TableCell align="right">{row.residues}</TableCell>
                                            <TableCell align="right">{row.t2}</TableCell>
                                            <TableCell align="right">{row.csp}</TableCell>
                                            <TableCell align="right">{row.std}</TableCell>
                                            <TableCell align="right">{row.affinity}</TableCell>
                                            <TableCell align="right">{row.mapping}</TableCell>
                                            <TableCell align="right">{row.smileformula}</TableCell>
                                            {/*https://www.codegrepper.com/code-examples/javascript/How+to+download+files+using+axios*/}
                                            {/*<TableCell align="right"><button onClick={() => (this.saveFile(row.sd1))}>Zip file</button></TableCell>*/}

                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
                )
                }
                {this.state.query1res.length > 0 && (
                    <div>
                        <TableContainer component={Paper}>
                            <Table sx={{minWidth: 650}} aria-label="simple table">
                                <TableHead>
                                  {/*  D1:         1H-1D
                                    D2:         WaterLOGSY
                                    D5:         not necessary (was special for this dataset)
                                    D20:      not necessary (pseudo2D STD)
                                    D21:      not necessary (STD-Reference)
                                    D22        STD
                                    D30:      not necessary (pseudo2D T2)
                                    D31:      T2_5ms
                                    D32        T2_100ms*/}
                                    <TableRow>
                                        <TableCell>ID</TableCell>
                                        <TableCell align="right">Mix name</TableCell>
                                        <TableCell align="right">Compaunds</TableCell>
                                        <TableCell align="right">1H-1D</TableCell>
                                        <TableCell align="right">WaterLOGSY</TableCell>
                                        {/*<TableCell align="right">D5</TableCell>*/}
                                        {/*<TableCell align="right">D20</TableCell>*/}
                                        {/*<TableCell align="right">D21</TableCell>*/}
                                        <TableCell align="right">STD</TableCell>
                                        {/*<TableCell align="right">D30</TableCell>*/}
                                        <TableCell align="right">T2_5ms</TableCell>
                                        <TableCell align="right">T2_100ms</TableCell>
                                    </TableRow>
                                </TableHead>
                                <TableBody>
                                    {this.state.query1res.map((row) => (
                                        <TableRow
                                            key={row.pid}
                                            sx={{'&:last-child td, &:last-child th': {border: 0}}}
                                        >
                                            <TableCell component="th" scope="row">
                                                {row.pid}
                                            </TableCell>
                                            <TableCell align="right">{row.mix}</TableCell>
                                            <TableCell align="right">{row.compaunds.map(f => (
                                                <Button
                                                    value={f}
                                                    color={(row.mapcompounds[f] === "Binding") ? "success" : "error" }
                                                    onClick={
                                                (e) => this.pressZid(e)
                                            } variant="text">{f}</Button>))}</TableCell>
                                            {/*https://www.codegrepper.com/code-examples/javascript/How+to+download+files+using+axios*/}
                                            {/*<TableCell align="right"><button onClick={() => (this.saveFile(row.sd1))}>Zip file</button></TableCell>*/}
                                            <TableCell align="right">
                                                <button onClick={() => (this.dowloadZip(row.pid, row.mix, "d1"))}> Zip
                                                </button>
                                            </TableCell>
                                            <TableCell align="right">
                                                <button onClick={() => (this.dowloadZip(row.pid, row.mix, "d2"))}> Zip
                                                </button>
                                            </TableCell>
                                           {/* <TableCell align="right">
                                                <button onClick={() => (this.dowloadZip(row.pid, row.mix, "d5"))}> Zip
                                                </button>
                                            </TableCell>
                                            <TableCell align="right">
                                                <button
                                                    onClick={() => (this.dowloadZip(row.pid, row.mix, "d20"))}> Zip
                                                </button>
                                            </TableCell>
                                            <TableCell align="right">
                                                <button
                                                    onClick={() => (this.dowloadZip(row.pid, row.mix, "d21"))}> Zip
                                                </button>
                                            </TableCell>*/}
                                            <TableCell align="right">
                                                <button
                                                    onClick={() => (this.dowloadZip(row.pid, row.mix, "d22"))}> Zip
                                                </button>
                                            </TableCell>
                                           {/* <TableCell align="right">
                                                <button
                                                    onClick={() => (this.dowloadZip(row.pid, row.mix, "d30"))}> Zip
                                                </button>
                                            </TableCell>*/}
                                            <TableCell align="right">
                                                <button
                                                    onClick={() => (this.dowloadZip(row.pid, row.mix, "d31"))}> Zip
                                                </button>
                                            </TableCell>
                                            <TableCell align="right">
                                                <button
                                                    onClick={() => (this.dowloadZip(row.pid, row.mix, "d32"))}> Zip
                                                </button>
                                            </TableCell>
                                        </TableRow>
                                    ))}
                                </TableBody>
                            </Table>
                        </TableContainer>
                    </div>
                )
                }
            </div>
        );
    }
}

export default withStyles(style)(UploadFiles);