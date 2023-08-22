import React, { useState } from 'react'
import { Paper, makeStyles, TableBody, TableRow, TableCell, Toolbar, InputAdornment } from '@material-ui/core';
import useTable from "./useTable";
import Controls from "../controls/Controls";
import { Search } from "@material-ui/icons";
import EditIcon from '@material-ui/icons/Edit';
import DeleteIcon from '@material-ui/icons/Delete';
import jsPDF from "jspdf";
import "jspdf-autotable";
import '../../pageutils/Button.css'

const useStyles = makeStyles(theme => ({
    pageContent: {
        overflowX: 'scroll',
        overflowY: 'auto',
        maxHeight: '90%',
        width: '100%',
        margin: theme.spacing(0),
        padding: theme.spacing(3)
    },
    container: {
        maxHeight: 450,
    },
    searchInput: {
        width: '30%',
        minWidth: '30%',
        left: 30,
    },
    newButton: {
        left: 50,
        color: '#fff',
        background: 'rgb(26, 23, 89)',
        '&:hover': {
            background: "#1888ff"
          }
    }
}))

const headCells = [
    { id: 'dbSchemaName', label: 'Schema' },
    { id: 'name', label: 'Name' },
    { id: 'type', label: 'Type' },
    { id: 'status', label: 'Status' },
    { id: 'description', label: 'Description' },
    { id: 'actions', label: 'Actions', disableSorting: true }
]

export default function ObjectsTable(props) {
    const classes = useStyles();
    const records = props.rows
    const schemas = props.schemas
    const [filterFn, setFilterFn] = useState({ fn: items => { return items; } })
    var [searchterms, setSearchTerms] = useState({search: ''})
    const userRole = localStorage.getItem('userrole')
    const firstSearch = schemas[0].id
    const [searchSchema, setSearchSchema] = useState({schema: firstSearch})

    const {
        TblContainer,
        TblHead,
        TblPagination,
        recordsAfterPagingAndSorting
    } = useTable(records, headCells, filterFn);

    const handleSearch = e => {
        let target = e.target;
        setSearchTerms({search: target.value.toLowerCase()})

        setFilterFn({
            fn: items => {
                if (target.value === null || target.value === '' || target.value === ' ')
                {
                    return items;
                }
                else
                {
                    
                    let filtered = items.filter(value => {
                        return (
                            value.name.toLowerCase().includes(target.value.toLowerCase()) ||
                            value.dbSchemaName.toLowerCase().includes(target.value.toLowerCase()) ||
                            value.type.toString().toLowerCase().includes(target.value.toLowerCase()) ||
                            value.status.toString().toLowerCase().includes(target.value.toLowerCase())
                          );
                    })
                    return filtered
                }
            }
        })
    }


    const openInPopup = item => {
        props.openInPopup(item)
    }

    const openInPopupRemove = item => {
        props.openInPopupRemove(item)
    }

    const exportTOPDF = () => {
        var data = []
        const unit = "pt";
        const size = "A4"; // Use A1, A2, A3 or A4
        const orientation = "landscape"; // portrait or landscape
    
        const marginLeft = 40;
        const doc = new jsPDF(orientation, unit, size);
        doc.setFontSize(15);
    
        const title = "Objects Export";
    
        if(searchterms.search === '')
        {
            data = records
        }
        else
        {
            data = records.filter(value => {
                return (
                    value.name.toLowerCase().includes(searchterms.search) ||
                    value.dbSchemaName.toLowerCase().includes(searchterms.search) ||
                    value.type.toString().toLowerCase().includes(searchterms.search) ||
                    value.status.toString().toLowerCase().includes(searchterms.search)
                  );
            })
        }

        const headers = [["Schema", "Object", "Object Type"]];
        const dataA = data.map(elt => [elt.dbSchemaName, elt.name, elt.type])

        let content = {
            startY: 50,
            head: headers,
            body: dataA
          };

      
          doc.text(title, marginLeft, 40);
          doc.autoTable(content);
          doc.save("report_objects.pdf")
    }

    const handleSelectChange = e => {
        let schema_name = e.target.value
        setSearchSchema({schema: schema_name})
        props.handleSelectSchemaChange(schema_name)
    }

    if(userRole === 'ADMIN')
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar className={classes.toolbar}>
                    <Controls.Select
                        name="schema"
                        label="Schema"
                        value={searchSchema.schema}
                        onChange={handleSelectChange}
                        options={schemas}
                    />
                    <Controls.Input
                        label="Search"
                        className={classes.searchInput}
                        InputProps={{
                            startAdornment: (<InputAdornment position="start">
                                <Search />
                            </InputAdornment>)
                        }}
                        onChange={handleSearch}
                    />
                    <Controls.Button
                        text="Export to PDF"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={() => {exportTOPDF()}}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                        <TblHead />
                        <TableBody key="objectsTable">
                            { 
                                recordsAfterPagingAndSorting().map((item, index) => {
                                    return (<TableRow key={index}>
                                        <TableCell>{item.dbSchemaName}</TableCell>
                                        <TableCell>{item.name}</TableCell>
                                        <TableCell>{item.type}</TableCell>
                                        <TableCell>{item.status}</TableCell>
                                        <TableCell>{item.description}</TableCell>
                                        <TableCell>
                                            <Controls.ActionButton
                                                color="primary"
                                                className={classes.editButton}
                                                title="Edit Object"
                                                onClick={() => { openInPopup(item) }}>
                                                <EditIcon fontSize="small" />
                                            </Controls.ActionButton>
                                            <Controls.ActionButton
                                                color="primary"
                                                title="Remove Object"
                                                className={classes.removeButton}
                                                onClick={() => { openInPopupRemove(item) }}>
                                                <DeleteIcon fontSize="small" />
                                            </Controls.ActionButton>
                                        </TableCell>
                                    </TableRow>)
                                })
                            }
                        </TableBody>
                </TblContainer>     
                <TblPagination />
            </Paper>
        )
    }
    else if (userRole === 'USER')
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Select
                        className={classes.Select}
                        name="schema"
                        label="Schema"
                        value={searchSchema.schema}
                        onChange={handleSelectChange}
                        options={schemas}
                    />
                    <Controls.Input
                        label="Search"
                        className={classes.searchInput}
                        InputProps={{
                            startAdornment: (<InputAdornment position="start">
                                <Search />
                            </InputAdornment>)
                        }}
                        onChange={handleSearch}
                    />
                    <Controls.Button
                        text="Export to PDF"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={() => {exportTOPDF()}}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.dbSchemaName}</TableCell>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.type}</TableCell>
                                    <TableCell>{item.status}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                    <TableCell>
                                        <Controls.ActionButton
                                            color="primary"
                                            className={classes.editButton}
                                            title="Edit Object"
                                            onClick={() => { openInPopup(item) }}>
                                            <EditIcon fontSize="small" />
                                        </Controls.ActionButton>
                                    </TableCell>
                                </TableRow>)
                            })
                        }
                    </TableBody>
                </TblContainer>     
                <TblPagination />
            </Paper>
        )
    }
    else
    {
        return (
            <Paper className={classes.pageContent}>
                <Toolbar>
                    <Controls.Select
                        className={classes.Select}
                        name="schema"
                        label="Schema"
                        value={searchSchema.schema}
                        onChange={handleSelectChange}
                        options={schemas}
                    />
                    <Controls.Input
                        label="Search"
                        className={classes.searchInput}
                        InputProps={{
                            startAdornment: (<InputAdornment position="start">
                                <Search />
                            </InputAdornment>)
                        }}
                        onChange={handleSearch}
                    />
                    <Controls.Button
                        text="Export to PDF"
                        variant="outlined"
                        className={classes.newButton}
                        onClick={() => {exportTOPDF()}}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                return (<TableRow key={index}>
                                    <TableCell>{item.dbSchemaName}</TableCell>
                                    <TableCell>{item.name}</TableCell>
                                    <TableCell>{item.type}</TableCell>
                                    <TableCell>{item.status}</TableCell>
                                    <TableCell>{item.description}</TableCell>
                                </TableRow>)
                            })
                        }
                    </TableBody>
                </TblContainer>     
                <TblPagination />
            </Paper>
        )
    }
}