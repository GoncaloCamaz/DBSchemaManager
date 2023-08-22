import React, {Component} from 'react';
import '../../index.css';
import axios from 'axios'
import Navbar from '../navbar/Navbar'
import Popup from '../../pageutils/Popup'
import AccessForm from '../forms/AccessForm'
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import TwoMessagesForm from '../forms/TwoMessagesForm';

class CreateAccessPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            schemas: [],
            dbobjects: [],
            dbusers: [],
            resultSetRows: [],
            recordForEdit: {},
            errorMessage: {},
            isLoaded: false,
            backenderrormessage: '',
            okMessage: '',
            popupAcknowledgeOpen: false,
            popupTwoMessagesOpen: false
        }
    }

    componentDidMount() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL + '/dbschema',authorization)
        .then(response => {
            this.setState({schemas: response.data.map(v => {return {id: v.name, value: v.name, title: v.name, label: v.name }})})
        })
        .catch(error => {
            console.log(error)
        })
        
        this.setState({isLoaded: true})
    }

    handleSchemaSelect = (schema) => {
        this.setState({recordForEdit: {schema: schema}})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        this.setState({dbusers: [], dbobjects: []})

        axios.get(backendURL + '/dbobject/dbschema/'+schema,authorization)
        .then(response => {
            this.setState({dbobjects: response.data.map(v => {return {id: v.name, value: v.name, title: v.name, label: v.name}})})
        })
        .catch(error => {
            console.log(error)
        })

        const toSend = {
            schema: schema
        }

        var url = backendURL+"/targetdatabase/"
        axios.post(url+'users',toSend,authorization)
            .then(response => this.handleResultSet(response.data))
            .catch(error => {
                if(error.response)
                {
                    this.setState({errorMessage: error.message})
                    this.setOpenPopupAcknowledge(true)
                }
            })
    }

    handleResultSet = (data) => {
        const columns = data.columns
        var j = 0
        if(columns.length === 2)
        {
            const rows =  data.rows
            for(j = 0; j < rows.length; j++)
            {
                const aux = "'" + rows[j].row[columns[0]] + "'@'" +rows[j].row[columns[1]]+"'"
                this.state.dbusers.push({id:aux, value: aux, title:aux, label: aux})
            }
        }
        else
        {
            const rows =  data.rows
            for(j = 0; j < rows.length; j++)
            {
                const aux = rows[j].row[columns[0]]
                this.state.dbusers.push({id:aux, value: aux, title:aux, label: aux})
            }
        }

        this.setState({isLoaded: true})
    }

    setOpenEditPopup = state => {
        this.setState({popupEditOpen: state})
    }

    setOpenAddPopup = state => {
        this.setState({popupAddOpen: state})
    }

    setOpenPopupRemove = state => {
        this.setState({popupRemoveOpen: state})
    }

    setViewDetails = state => {
        this.setState({viewDetails: state})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    setOpenTwoMessages = state => {
        this.setState({popupTwoMessagesOpen: state})
    }

    addAccess = (access, resetForm) => {
        if(access)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            const toSend = {
                dbSchemaName: access.schema,
                dbObjectName: access.dbobject,
                dbUsername: access.user,
                privilege: access.privilege,
                dbSchemaManagerUsername: localStorage.getItem("username"),
                description: access.description,
                permission: access.permission
            }

            var url = backendURL+"/dbaccess/save"
                axios.post(url,toSend,requestparams)
                    .then((response) => {
                        if(response.data.validQuery === false)
                        {
                            this.setState({backenderrormessage: response.data.errormessage})
                            this.setOpenPopupAcknowledge(true)
                        }
                        else
                        {
                            resetForm()
                            this.setState({recordForEdit: {}, okMessage: toSend.permission + ' ' + toSend.privilege + ' to user ' + toSend.dbUsername})
                            this.setOpenAddPopup(false)
                            this.setOpenTwoMessages(true)
                        }
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


    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    handleOk = () => {
        this.setOpenTwoMessages(false)
    }

    render() {
        const { isLoaded, schemas } = this.state;

        if(!isLoaded){
            return (
                <div>
                    <Navbar/>
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
                                <br/>
                                <div className='new_access-container'>
                                    <AccessForm 
                                        addOrEdit={this.addAccess}
                                        schemas={schemas}
                                        dbobjects={this.state.dbobjects}
                                        users={this.state.dbusers}
                                        handleSchemaSelect={this.handleSchemaSelect}
                                    />
                                    <Popup 
                                        title={'Something went wrong'}
                                        openPopup={this.state.popupAcknowledgeOpen}
                                        setOpenPopup={this.setOpenPopupAcknowledge}>
                                        <AcknowledgeForm 
                                            message={this.state.backenderrormessage}
                                            handleacknowledge={this.handleacknowledge}
                                        />
                                    </Popup>
                                    <Popup 
                                        title={'Access Operation Result'}
                                        openPopup={this.state.popupTwoMessagesOpen}
                                        setOpenPopup={this.setOpenTwoMessages}
                                    >
                                        <TwoMessagesForm 
                                            mainMessage={'Success!'}
                                            secundaryMessage={this.state.okMessage}
                                            handleOk={this.handleOk}
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

export default CreateAccessPage