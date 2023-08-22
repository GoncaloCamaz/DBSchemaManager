import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import Popup from '../../pageutils/Popup'
import SchemaForm from '../forms/SchemaForm'
import SchemaEditForm from '../forms/SchemaEditForm'
import SchemaCredentialsForm from '../forms/SchemaCredentialsForm'
import ConfirmForm from '../forms/ConfirmForm';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import SchemaTable from '../tables/SchemaTable';
import { Redirect } from 'react-router-dom';
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class SchemaPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            platforms: [],
            schemas: [],
            currentSchema: {},
            acknowledgePopupTitle: '',
            recordForEdit: {},
            errorMessage: '',
            recordForRemove: {},
            schemaForEditor: '',
            isLoadingSchemas: true,
            isLoaded: false,
            popupAddOpen: false,
            popupEditOpen: false,
            popupEditCredentialsOpen: false,
            popupRemoveOpen: false,
            popupAcknowledgeOpen: false,
            sendQueryPage: false
        }
    }
    
    componentDidMount() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        axios.get(backendURL + '/platform',authorization)
        .then(response => {
            response.data.forEach(p =>
                this.state.platforms.push({id: p.name, value: p.name, title: p.name, label: p.name}) )
        })
        .catch(error => {
            if(error)
                this.setState({isLoaded: true, errorMessage: error.message, popupAcknowledgeOpen: true})
        })

        this.schemasTableUpdate()
    }

    schemasTableUpdate() 
    {
        this.setState({isLoaded: false})
        this.setState({schemas: []})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        axios.get(backendURL +'/dbschema',authorization)
        .then(response => {
            this.setState({schemas: response.data})
        })
        .catch(error => {
            console.log(error)
        })
        this.setState({isLoaded: true})
    }

    setOpenEditPopup = state => {
        this.setState({popupEditOpen: state})
    }

    setOpenEditCredentialsPopup = state => {
        this.setState({popupEditCredentialsOpen: state})
    }

    setOpenAddPopup = state => {
        this.setState({popupAddOpen: state})
    }

    setOpenPopupRemove = state => {
        this.setState({popupRemoveOpen: state})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    addSchema = (schema, resetForm) => {
        if(schema)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            schema = {...schema}
            this.setState({isLoaded: false})
            var url = backendURL + "/dbschema/"
            
            axios.post(url+'save',schema,requestparams)
                .then((response) => {
                    if(response.data.statuscode === 201)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenAddPopup(false)
                        this.schemasTableUpdate()
                    }
                    else
                    {
                        this.setState({acknowledgePopupTitle: response.data.warningmessage})
                        this.setState({errorMessage: response.data.errormessage})
                        this.setOpenPopupAcknowledge(true)
                        this.setState({isLoaded: true})
                    }
                })
                .catch(_error => {
                    this.setState({acknowledgePopupTitle: 'Something went wrong!'})
                    this.setState({errorMessage: 'Please check connections with backend Service!'})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded:true})
                })
        }
    }

    editSchema = (schema, resetForm) => {
        if(schema)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            schema = {
                name: schema.name, 
                connectionstring: schema.connectionstring,
                platformname: schema.platformname, 
                updateperiod: schema.updateperiod,
                description: schema.description,
                sqlservername: schema.sqlservername
            }

            var url = backendURL+"/dbschema/"
                axios.post(url+'update',schema,requestparams)
                .then((response) => {
                    if(response.data.statuscode === 200)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditPopup(false)
                        this.schemasTableUpdate()
                    }
                    else
                    {
                        this.setState({acknowledgePopupTitle: response.data.warningmessage})
                        this.setState({errorMessage: response.data.errormessage})
                        this.setOpenPopupAcknowledge(true)
                        this.setState({isLoaded: true})
                    }
                })
                .catch(_error => {
                    this.setState({acknowledgePopupTitle: 'Something went wrong!'})
                    this.setState({errorMessage: 'Please check connections with backend Service!'})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded:true})
                })
            }
    }

    editSchemaCredentials = (schema, resetForm) => {
        if(schema)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            schema = {...schema}
            var url = backendURL + "/dbschema/update/credentials"
                axios.post(url,schema,requestparams)
                .then((response) => {
                    if(response.data.statuscode === 200)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditCredentialsPopup(false)
                        this.schemasTableUpdate()
                    }
                    else{
                        this.setState({acknowledgePopupTitle: response.data.warningmessage})
                        this.setState({errorMessage: response.data.errormessage})
                        this.setOpenPopupAcknowledge(true)
                        this.setState({isLoaded: true})
                    }
                })
                .catch(_error => {
                    this.setState({acknowledgePopupTitle: 'Something went wrong!'})
                    this.setState({errorMessage: 'Please check connections with backend Service!'})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded:true})
                })
            }
    }

    /**
     * Function to remove a given schema
     * @param {*} schema 
     */
    removeSchema = (schema) => {
        if(schema) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }
            }

            var url = backendURL + "/dbschema/delete/"+schema.name
            axios.delete(url, requestparams)
            .then((response) => {
                if(response.data.statuscode === 200)
                {
                    this.setOpenPopupRemove(false)
                    this.schemasTableUpdate()
                }
                else
                {
                    this.setState({acknowledgePopupTitle: response.data.warningmessage})
                    this.setState({errorMessage: response.data.errormessage})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded: true})
                }
            })
            .catch(error => {
                this.setState({acknowledgePopupTitle: 'Something went wrong!'})
                this.setState({errorMessage: 'Please check connections!'})
                this.setOpenPopupAcknowledge(true)
                this.setState({isLoaded:true})
            })
        }
    }

    openInPopup = item => {
        if(item !== null)
        {
            this.setState({recordForEdit: item})
            this.setOpenEditPopup(true)
        }
        else{
            this.setOpenAddPopup(true)
        }

    }

    openEditCredentialsPopup = item => {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        axios.get(backendURL + '/dbschema/name/'+item.name+'/credentials',authorization)
        .then(response => {this.setState({recordForEdit: response.data})})
        .catch(error => {
            if(error)
                this.setState({isLoaded: true, errorMessage: error, popupAcknowledgeOpen: true})
        })
        this.setState({isLoaded: true})
        this.setOpenEditCredentialsPopup(true)
    }

    startOnDemandUpdate = item => {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        const schema = {...item}
        var url = backendURL + "/dbschema/update/content"
            axios.post(url,schema,authorization)
            .then((response) => {
                if(response.status === 200)
                {
                    this.schemasTableUpdate()
                }
                else
                {
                    this.setState({acknowledgePopupTitle: "Something went wrong!"})
                    this.setState({errorMessage: "Please check connections!"})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded: true})
                }
            })
            .catch(error => {
                this.setState({acknowledgePopupTitle: 'Something went wrong!'})
                this.setState({errorMessage: 'Please check connections!'})
                this.setOpenPopupAcknowledge(true)
                this.setState({isLoaded:true})
            })
    }

    openInPopupRemove = item => {
        this.setState({recordForRemove: item})
        this.setOpenPopupRemove(true)
    }
    
    handleAddSchema = () => {
        this.setOpenAddPopup(true)
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    handleQuery = item => {
        this.setState({ schemaForEditor: item.name, sendAQuery: true})
    }

    render() {
        const { isLoaded, sendAQuery } = this.state;

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
        else if(sendAQuery)
        {
            return <Redirect to={{
                pathname:"schemas/query",
                state: {currentSchema: this.state.schemaForEditor}
            }}/>
        }
        else {
             return (
                <div>
                <Navbar />
                <div className='page_wrapper'>
                    <div className='page-container'>
                        <div className='table-container'>
                            <SchemaTable 
                                rows={this.state.schemas} 
                                addOrEdit={this.editSchema}
                                openInPopup={this.openInPopup}
                                openEditCredentialsPopup={this.openEditCredentialsPopup}
                                openInPopupRemove={this.openInPopupRemove}
                                sendAQuery={this.handleQuery}
                                startOnDemandUpdate={this.startOnDemandUpdate}
                            />
                            <Popup 
                                title={'Edit Schema'}
                                openPopup={this.state.popupEditOpen}
                                setOpenPopup={this.setOpenEditPopup}>
                                <SchemaEditForm 
                                    recordForEdit ={this.state.recordForEdit}
                                    addOrEdit={this.editSchema}
                                    platforms={this.state.platforms}
                                />
                            </Popup>
                            <Popup 
                                title={'Schema Connection Credentials'}
                                openPopup={this.state.popupEditCredentialsOpen}
                                setOpenPopup={this.setOpenEditCredentialsPopup}>
                                <SchemaCredentialsForm 
                                    recordForEdit ={this.state.recordForEdit}
                                    addOrEdit={this.editSchemaCredentials}
                                />
                            </Popup>
                            <Popup 
                                title={'Add New schema'}
                                openPopup={this.state.popupAddOpen}
                                setOpenPopup={this.setOpenAddPopup}>
                                <SchemaForm 
                                    addOrEdit={this.addSchema}
                                    platforms={this.state.platforms}
                                />
                            </Popup>
                            <Popup 
                                title="Do you want to remove this schema?"
                                openPopup={this.state.popupRemoveOpen}
                                setOpenPopup={this.setOpenPopupRemove}>
                                <ConfirmForm 
                                    warningmessage={"This action will remove all objects from "
                                        + this.state.recordForRemove.name + " schema."}
                                    recordForRemove={this.state.recordForRemove}
                                    removeFunction={this.removeSchema}
                                />
                            </Popup>
                            <Popup 
                                title={this.state.acknowledgePopupTitle !== '' ? this.state.acknowledgePopupTitle : 'Something went wrong!'}
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

export default SchemaPage