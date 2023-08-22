import React, {Component} from 'react';
import '../../index.css';
import Navbar from '../navbar/Navbar'
import AccessesTable from '../tables/AccessesTable';
import './Pages.css'
import axios from 'axios'
import { backendURL } from '../../constants';
import Popup from '../../pageutils/Popup'
import ConfirmForm from '../forms/ConfirmForm';
import AccessEditForm from '../forms/AccessEditForm';
import AcknowledgeForm from '../forms/AcknowledgeForm';

export default class AccessHistoryPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            schemas: [],
            currentSchema: '',
            accesses: [],
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
        this.accessesTableUpdate(true)
    }

    async accessesTableUpdate(updateSchema) {
        this.setState({isLoaded: false})

        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        
        if(this.state.currentSchema === '' || this.state.currentSchema === 'All')
        {
            axios.get(backendURL+'/dbaccess',authorization)
            .then(response => {
                this.setState({
                    accesses: response.data
                })
            })
            .catch(error => {
                console.log(error)
            })
        }
        else
        {
            axios.get(backendURL+'/dbaccess/dbschema/'+this.state.currentSchema,authorization)
            .then(response => {
                this.setState({
                    accesses: response.data
                })
            })
            .catch(error => {
                console.log(error)
            })
        }

        if(updateSchema === true)
        {
            var schemasList = [{key: "All", value: "All", title: "All", id: "All"}]
            await axios.get(backendURL+'/dbschema',authorization)
                .then(response => {
                    response.data.forEach(e => 
                        schemasList.push({id: e.name, key: e.name, value: e.name, title: e.name}))
                })
                .catch(error => {
                    console.log(error)
            })
            this.setState({
                schemas: schemasList, 
            }, () => {
                this.setState({isLoaded: true})
            })
        }
        this.setState({isLoaded: true})
    }

    editAccess = (access, resetForm) => {
        if(access)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            const toSend = {
                dbSchemaName: access.dbSchemaName,
                dbObjectName: access.dbObjectName,
                dbUsername: access.dbUsername,
                privilege: access.privilege,
                dbSchemaManagerUsername: localStorage.getItem("username"),
                description: access.description,
                permission: access.permission,
                timestamp: access.timestamp
            }

            var url = backendURL+"/dbaccess/update"
                axios.post(url,toSend,requestparams)
                    .then((_response) => {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditPopup(false)
                        this.accessesTableUpdate(false)
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
    
    /**
     * Function to remove a given access
     * @param {*} access
     */
     removeAccess = (access) => {
        if(access) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }  
            }
            const timestamp = access.timestamp.toString()
            var url = backendURL+"/dbaccess/delete/"+access.dbSchemaName+"/" +
                access.dbObjectName+"/"+access.dbUsername + "/"+ access.privilege + "/" + timestamp
            axios.delete(url, requestparams)
            .then((_response) => {
                this.setState({recordForRemove: {}})
                this.setOpenPopupRemove(false)
                this.accessesTableUpdate(false)
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

    setOpenEditPopup = state => {
        this.setState({popupEditOpen: state})
    }

    setOpenPopupRemove = state => {
        this.setState({popupRemoveOpen: state})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    openInPopup = item => {
        this.setState({recordForEdit: item})
        this.setOpenEditPopup(true)
    }

    openInPopupRemove = item => {
        this.setState({recordForRemove: item})
        this.setOpenPopupRemove(true)
    }

    handleSelectSchemaChange = item => {
        this.setState({
            currentSchema: item, 
        }, () => {
            this.accessesTableUpdate(false)
        })
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    render(){
        const { isLoaded, schemas } = this.state;

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
        else
        {
            return (
                <div>
                    <Navbar />
                    <div className='page_wrapper'>
                        <div className='page-container'>
                            <div className='table-container'>
                                <AccessesTable 
                                    rows={this.state.accesses}
                                    schemas={schemas}
                                    openInPopup={this.openInPopup}
                                    openInPopupRemove={this.openInPopupRemove}
                                    handleSelectSchemaChange={this.handleSelectSchemaChange}
                                />
                                 <Popup 
                                        title={'Edit Access Description'}
                                        openPopup={this.state.popupEditOpen}
                                        setOpenPopup={this.setOpenEditPopup}>
                                        <AccessEditForm 
                                            recordForEdit ={this.state.recordForEdit}
                                            addOrEdit={this.editAccess}
                                        />
                                </Popup>
                                <Popup 
                                        title={'Do you want to remove this Access?'}
                                        openPopup={this.state.popupRemoveOpen}
                                        setOpenPopup={this.setOpenPopupRemove}>
                                        <ConfirmForm 
                                            warningmessage={
                                                "Access " +
                                                this.state.recordForRemove.permission +
                                                " " + this.state.recordForRemove.privilege +
                                                " on " + this.state.recordForRemove.dbObjectName +
                                                " to " + this.state.recordForRemove.dbUsername +
                                                " will ONLY be deleted from DBSchema Manager database."}
                                            recordForRemove={this.state.recordForRemove}
                                            removeFunction={this.removeAccess}
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