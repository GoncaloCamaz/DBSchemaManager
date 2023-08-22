import React, { useState } from 'react'
import { Paper, makeStyles, TableBody, TableRow, TableCell, Toolbar, InputAdornment } from '@material-ui/core';
import useTable from "./useTable";
import Controls from "../controls/Controls";
import { Search } from "@material-ui/icons";
import Popup from '../../pageutils/Popup'
import jsPDF from "jspdf";
import "jspdf-autotable";
import '../../pageutils/Button.css'
import SelectColumnsForm from '../forms/SelectColumnsForm';

const useStyles = makeStyles(theme => ({
    pageContent: {
        overflowX: 'scroll',
        overflowY: 'scroll',
        maxHeight: '100%',
        width: '100%',
        margin: theme.spacing(0),
        padding: theme.spacing(3)
    },
    container: {
        maxHeight: '300px',
    },
    toolbar: {
        width: '100%',
        display: 'flex',
        flexDirection: 'flex-start',
        
    },
    searchInput: {
        width: '30%',
        minWidth: '50%',
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

export default function DefaultTable(props) {
    const classes = useStyles();
    const records = props.rows
    const headCells = props.columns
    const columnNames = props.searchColumns
    const [filterFn, setFilterFn] = useState({ fn: items => { return items; } })
    const [searchterms, setSearchTerms] = useState({search: ''})
    const firstSearch = columnNames[0].id
    const [searchColumn, setSearchColumn] = useState({column: firstSearch})
    const [openSelectHeadersPopup, setOpenSelectHeadersPopup] = useState(false)

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
                    const columntoSearch = searchColumn.column
                    const searchvalue = target.value.toString().toLowerCase()
                    let filtered = items.filter(value => {
                        return value[columntoSearch].toString().toLowerCase().includes(searchvalue)
                    })
                    return filtered
                }
            }
        })
    }

    const openSelectPopup = () => {
        setOpenSelectHeadersPopup(true)
    }

    const exportTOPDF = (headersToExport) => {
        var data = []
        const unit = "pt";
        const size = "A4"; // Use A1, A2, A3 or A4
        const orientation = "landscape"; // portrait or landscape
        const doc = new jsPDF(orientation, unit, size);
        doc.setFontSize(10);
        
        if(searchterms.search === '')
        {
            data = records
        }
        else
        {
            const columntoSearch = searchColumn.column
            const searchvalue = searchterms.search.toString().toLowerCase()
            data = records.filter(value => {
                return value[columntoSearch].toString().toLowerCase().includes(searchvalue)
            })
        }

        var headers = [];
        headersToExport.forEach(elm => headers.push(elm.name))
        var j
        var dataA = data.map(elt => 
            {
                var aux = []
                for(j = 0; j < headersToExport.length; j++ )
                {
                    aux.push(elt[headersToExport[j].name])
                }
                return aux
            })

        let content = {
            startY: 50,
            head: [headers],
            body: dataA
          };
      
          doc.autoTable(content);
          doc.save("table_export.pdf")
    }

    const handleSelectChange = e => {
        let column_name = e.target.value
        setSearchColumn({column: column_name})
    }

    const setSelectedHeaders = (content) => {
        exportTOPDF(content)
        setOpenSelectHeadersPopup(false)
    }

    return (
            <Paper className={classes.pageContent}>
                <Popup
                    title={"Select Columns to Export"}
                    openPopup={openSelectHeadersPopup}
                    setOpenPopup={setOpenSelectHeadersPopup}
                >
                    <SelectColumnsForm 
                        headers={columnNames}
                        selectedHeaders={setSelectedHeaders}
                    />
                </Popup>
                <Toolbar className={classes.toolbar}>
                    <Controls.Select
                        name="column"
                        label="Column"
                        value={searchColumn.column}
                        onChange={handleSelectChange}
                        options={columnNames}
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
                        onClick={() => {openSelectPopup()}}
                    />
                </Toolbar>
                <TblContainer className={classes.container}>
                    <TblHead />
                    <TableBody>
                        { 
                            recordsAfterPagingAndSorting().map((item, index) => {
                                var cells = []
                                for(var i = 0 ; i < headCells.length; i++)
                                {
                                    const cellname = headCells[i].id
                                    cells.push(<TableCell key={cellname+""+index}>{item[cellname]}</TableCell>)
                                }
                                return (<TableRow key={index}>
                                   {cells}
                                </TableRow>)
                            })
                        }
                    </TableBody>
                </TblContainer>
                <TblPagination />
            </Paper>
    )
}