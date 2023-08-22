import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import ColumnsTable from '../tables/ColumnsTable'
import Popup from '../../pageutils/Popup'
import ObjectForm from '../forms/ObjectForm'
import ConfirmForm from '../forms/ConfirmForm';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class ColumnsPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            schemas: [],
            currentSchema: '',
            columns: [],
            isLoaded: false,
            recordForRemove: {},
            recordForEdit: {},
            errorMessage: {},
            popupRemoveOpen: false,
            popupEditOpen: false,
            popupAcknowledgeOpen: false
        }
    }
    
    componentDidMount() {
        this.columnsTableUpdate(true)
    }

    columnsTableUpdate(updateSchema){
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        if(this.state.currentSchema === '' || this.state.currentSchema === 'All')
        {
            axios.get(backendURL+'/dbcolumn',authorization)
            .then(response => {
                this.setState({columns: response.data})
            })
            .catch(error => {
                console.log(error)
            })
        }
        else
        {
            axios.get(backendURL+'/dbcolumn/dbschema/'+this.state.currentSchema,authorization)
            .then(response => {
                this.setState({columns: response.data})
            })
            .catch(error => {
                console.log(error)
            })
        }

        if(updateSchema === true)
        {
            this.setState({schemas: [{key: "All", value: "All", title: "All", id: "All"}]})
            axios.get(backendURL+'/dbschema',authorization)
                .then(response => {
                    response.data.forEach(e => 
                        this.state.schemas.push({id: e.name, key: e.name, value: e.name, title: e.name}))
                })
                .catch(error => {
                    console.log(error)
            })
        }

        this.setState({isLoaded: true})
    }

    setOpenEditPopup = state => {
        this.setState({popupEditOpen: state})
    }

    setOpenPopupRemove = state => {
        this.setState({popupRemoveOpen: state})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    editColumn = (dbColumn, resetForm) => {
        if(dbColumn)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            dbColumn = {...dbColumn}

            var url = backendURL+"/dbcolumn/update"
            axios.post(url,dbColumn,requestparams)
                .then((response => {
                    resetForm()
                    this.setState({recordForEdit: {}})
                    this.setOpenEditPopup(false)
                    this.columnsTableUpdate(false)
                }))
                .catch(error => {
                    if(error.response)
                    {
                        this.setState({errorMessage: error.message})
                        this.setOpenPopupAcknowledge(true)
                    }
                })
        }
    }

    /**
     * Function to remove a given Column
     * @param {*} Column
     */
    removeColumn = (dbcolumn) => {
        if(dbcolumn) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }  
            }

            var url = backendURL+"/dbcolumn/delete/"+dbcolumn.dbSchemaName+"/" +
                dbcolumn.dbObjectName+"/"+dbcolumn.name
            axios.delete(url, requestparams)
            .then((_response) => {
                this.setState({recordForRemove: {}})
                this.setOpenPopupRemove(false)
                this.columnsTableUpdate(false)
            })
            .catch(error => {
                if(error.response)
                {
                    this.setState({errorMessage: error.message})
                    this.setOpenPopupAcknowledge(true)
                }
            })
        }
    }

    openInPopup = item => {
        this.setState({recordForEdit: item})
        this.setOpenEditPopup(true)
    }

    openInPopupRemove = item => {
        this.setState({recordForRemove: item})
        this.setOpenPopupRemove(true)
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    handleSelectSchemaChange = item => {
        this.setState({
            currentSchema: item, 
        }, () => {
            this.columnsTableUpdate(false)
        })
    }

    render() {
        const { isLoaded } = this.state;

        if(!isLoaded){
           return (
               <div>
                   <Navbar />
                    <div className='page_wrapper'>
                        <h4 className='blinkText'>Loading..</h4>
                    </div>
                </div>
          )
        }
        else {
             return (
                 <div>
                    <Navbar />
                        <div className='page_wrapper'>
                            <div className='page-container'>
                                <div className='table-container'>
                                    <ColumnsTable 
                                        rows={this.state.columns} 
                                        schemas={this.state.schemas}
                                        handleSelectSchemaChange={this.handleSelectSchemaChange}
                                        openInPopup={this.openInPopup}
                                        openInPopupRemove={this.openInPopupRemove}/>
                                    <Popup 
                                        title={'Edit Column'}
                                        openPopup={this.state.popupEditOpen}
                                        setOpenPopup={this.setOpenEditPopup}>
                                        <ObjectForm 
                                            recordForEdit ={this.state.recordForEdit}
                                            addOrEdit={this.editColumn}
                                        />
                                    </Popup>
                                    <Popup 
                                        title={'Do you want to remove the Column?'}
                                        openPopup={this.state.popupRemoveOpen}
                                        setOpenPopup={this.setOpenPopupRemove}>
                                        <ConfirmForm 
                                            warningmessage={
                                                "Column " +
                                                this.state.recordForRemove.name +
                                                " will be deleted from DBSchema Manager database."}
                                            recordForRemove={this.state.recordForRemove}
                                            removeFunction={this.removeColumn}
                                        />
                                    </Popup>
                                    <Popup 
                                        title={'Something went wrong'}
                                        openPopup={this.state.popupAcknowledgeOpen}
                                        setOpenPopup={this.setOpenPopupAcknowledge}>
                                        <AcknowledgeForm 
                                            message={this.state.errorMessage}
                                            handleacknowledge={this.handleacknowledge}
                                        />
                                    </Popup>
                                </div>
                            </div>
                        </div>
                    </div>
            ) 
        }
    }
}

export default ColumnsPage