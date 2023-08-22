import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import CreateViewResultTable from '../tables/CreateViewResultTable'
import ViewCreationForm from '../forms/ViewCreationForm'
import AcknowledgeForm from '../forms/AcknowledgeForm'
import OverridePermissionForm from '../forms/OverridePermissionForm'
import DefaultTable from '../tables/DefaultTable'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'
import Button from '../controls/Button'
import Popup from '../../pageutils/Popup'

class CreateViewPage extends Component {
    constructor(props) {
        super(props)
        
        this.state = {
            schemas: [],
            currentSchema: '',
            currentSchemaInfo: {},
            rows: [],
            resultSetColumns: [],
            searchColumns: [],
            resultSetRows: [],
            recordForEdit: {},
            hideEditor: false,
            backenderrormessage: '',
            backendwarningmessage: '',
            history: null,
            validQuery: false,
            isLoaded: false,
            errorMessage: '',
            errorMessageTitle: 'Something went wrong',
            popupAcknowledgeOpen: false,
            popupBackendWarning: false,
            objectView: {}
        }
    }
    
    componentDidMount() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        if(this.props.location.state !== undefined)
        {
            const code = this.props.location.state.codeFromHistory
            const schema = this.props.location.state.schemaFromHistory
            const viewname = this.props.location.state.viewNameFromHistory
            if(code.substring(0, 8).toLowerCase().includes("create"))
            {
                this.setState({history: code, currentSchema: schema})
            }
            else
            {
                const newcode = "create or replace view " + viewname + " as " + code
                this.setState({history: newcode, currentSchema: schema})
            }
        }
        
        axios.get(backendURL+'/dbschema',authorization)
        .then(response => {
            this.setState({schemas: response.data.map(v => {return {id: v.name, value: v.name, title: v.name, label: v.name}})})
        })
        .catch(error => {
            console.log(error)
        })
        
        this.setState({isLoaded: true})
    }

    handleSelectSchemaChange = (value) => {
        this.setState({currentSchema: value})
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        axios.get(backendURL+'/dbcolumn/dbschema/' + value,authorization)
                .then(response => {
                    this.setState({rows: response.data})
                })
                .catch(error => {
                    console.log(error)
                })
        this.setState({isLoaded: true})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    setOpenPopupBackendWarning = state => {
        this.setState({popupBackendWarning: state})
    }

    handleSchemaReset() {
        this.setState({currentSchema: ''})
        this.setState({currentSchemaInfo: null})
        this.setState({rows: []})
    }

    sendScript(script){
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        var sendsudo = false
        if(script.sudo === true)
        {
            sendsudo=true
        }

        const toSend = {
            code: this.state.objectView.code,
            username: this.state.objectView.username, 
            password: this.state.objectView.password,
            dbSchemaName: this.state.objectView.dbSchemaName,
            sudo: sendsudo,
            definer: localStorage.getItem('username')
        }
        
        var url = backendURL+"/dbscript/send"
        axios.post(url,toSend,authorization)
        .then(response => this.handleResultSet(response.data))
        .catch(error => {
            if(error.response)
            {
                this.setState({errorMessage: error.message, errorMessageTitle: 'Something went wrong'})
                this.setOpenPopupAcknowledge(true)
            }
        })
    }

    /**
     * This function saves information to this component state to prevent
     * cenarios where the code is not valid or a warning is received
     * objectView represents information about view that will be sent
     * recordForEdit is used to keep data on form if something fails
     * 
     * after updating the state of the component, send script function is called
     * @param {*} script 
     */
    addScript = (script) => {
        this.setState({isLoaded: false})
        if((script.script !== undefined ) && (script.name !== undefined))
        {
            if(script.username !== undefined && script.password !== undefined)
            {
                this.setState({objectView: {
                    code: script.script,
                    dbSchemaName: script.name,
                    username: script.username,
                    password: script.password
                }, recordForEdit: {
                    script: script.script,
                    name: script.name,
                    username: script.username,
                    password: script.password
                }}, () => {
                    this.sendScript(script)
                })
            }
            else
            {
                this.setState({objectView: {
                    code: script.script,
                    dbSchemaName: script.name,
                    username: '',
                    password: ''
                },  recordForEdit: {
                    script: script.script,
                    name: script.name
                }}, () => {
                    this.sendScript(script)
                })
            }
        }
        else
        {
            this.sendScript(script)
        }
        
    }

    handleResultSet = (data) => {
        this.setState({isLoaded: false})
        this.setState({resultSetColumns: []})
        this.setState({resultSetRows: []})
        this.setState({searchColumns: []})

        const columns = data.columns
        const errorMessage = data.errormessage
        const warningMessage = data.warningmessage

        if(data.validQuery === false)
        {
            this.setState({validQuery: false})
            if(errorMessage === '' && warningMessage === '')
            {
                this.setState({errorMessage: 'Please check connections with Fetcher service!'})
                this.setOpenPopupAcknowledge(true)
            }
            else
            {
                this.setState({backendwarningmessage: warningMessage})
                this.setState({backenderrormessage: errorMessage})
                this.setOpenPopupAcknowledge(true)
            }
        }
        else
        {
            for(var i = 0; i < columns.length; i++)
            {
                this.state.resultSetColumns.push({id: columns[i], 
                    key: columns[i], 
                    label: columns[i]}
                )
                this.state.searchColumns.push({
                    id: columns[i],
                    key: columns[i],
                    title: columns[i],
                    label: columns[i],
                    value: columns[i]
                })
            }
    
            const rows =  data.rows
            for(var j = 0; j < rows.length; j++)
            {
                this.state.resultSetRows.push(rows[j].row)
            }
            this.setState({validQuery: true})
        }
        
        this.setState({isLoaded: true})
    }

    handleReturn = () => {
        this.setState({validQuery: false})
        this.setState({resultSetColumns: [], resultSetRows: []})
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    handleOverride = () => {
        const toSend = {
            script: this.state.objectView.code,
            definer: localStorage.getItem('username'),
            username: this.state.currentSchemaInfo.username,
            password: this.state.currentSchemaInfo.password,
            sudo: true
        }
        this.setOpenPopupBackendWarning(false)
        this.addScript(toSend)
    }

    handleCancel = () => {
        this.setOpenPopupBackendWarning(false)
    }

    hideEditor = () => {
        this.setState({hideEditor: !this.state.hideEditor})
    }

    render() {
        const { isLoaded, validQuery, schemas, hideEditor, errorMessageTitle } = this.state;

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
        else if(validQuery === false) {
                return (
                    <div>
                        <Navbar />
                            <div className='page_wrapper'>
                                <div className='page-container'>
                                    <div className='smalltable-container'>
                                        <CreateViewResultTable 
                                            rows={this.state.rows}
                                        />
                                        <div className='page-info'>
                                            <ViewCreationForm
                                                schemas = {schemas} 
                                                scriptfromhistory={this.state.history}
                                                handleSelectSchemaChange={this.handleSelectSchemaChange}
                                                addOrEdit={this.addScript} 
                                                recordForEdit={this.state.recordForEdit}
                                                currentSchema={this.state.currentSchema} 
                                            />
                                        </div>
                                    </div>
                                    <Popup 
                                        title={errorMessageTitle}
                                        openPopup={this.state.popupAcknowledgeOpen}
                                        setOpenPopup={this.setOpenPopupAcknowledge}>
                                        <AcknowledgeForm 
                                            message={this.state.backenderrormessage}
                                            handleacknowledge={this.handleacknowledge}
                                        />
                                    </Popup>
                                    <Popup 
                                        title={errorMessageTitle}
                                        openPopup={this.state.popupBackendWarning}
                                        setOpenPopup={this.setOpenPopupBackendWarning}>
                                        <OverridePermissionForm 
                                            errormessage={this.state.backenderrormessage}
                                            warningmessage={this.state.backendwarningmessage}
                                            handleOverride={this.handleOverride}
                                            handleCancel={this.handleCancel}
                                        />
                                    </Popup>
                                </div>
                            </div>
                    </div>
                )             
        }
        else if (!hideEditor)
        {
            return (
                <div>
                    <Navbar />
                        <div className='page_wrapper'>
                            <div className='page-container'>
                                <div className='smalltable-container'>
                                        <DefaultTable
                                            columns = {this.state.resultSetColumns} 
                                            rows={this.state.resultSetRows}
                                            searchColumns={this.state.searchColumns}
                                        />
                                    <div className='page-info'>
                                        <ViewCreationForm
                                            schemas = {schemas} 
                                            scriptfromhistory={this.state.history}
                                            handleSelectSchemaChange={this.handleSelectSchemaChange}
                                            addOrEdit={this.addScript} 
                                            recordForEdit={this.state.recordForEdit} 
                                            currentSchema={this.state.currentSchema} 
                                        />
                                        <Button
                                            text={"Hide Editor"}
                                            onClick={this.hideEditor}
                                        />
                                    </div>
                                </div>
                                <Popup 
                                    title={errorMessageTitle}
                                    openPopup={this.state.popupAcknowledgeOpen}
                                    setOpenPopup={this.setOpenPopupAcknowledge}>
                                    <AcknowledgeForm 
                                        message={this.state.backenderrormessage}
                                        handleacknowledge={this.handleacknowledge}
                                    />
                                </Popup>
                                <Popup 
                                    title={errorMessageTitle}
                                    openPopup={this.state.popupBackendWarning}
                                    setOpenPopup={this.setOpenPopupBackendWarning}>
                                    <OverridePermissionForm 
                                        errormessage={this.state.backenderrormessage}
                                        warningmessage={this.state.backendwarningmessage}
                                        handleOverride={this.handleOverride}
                                        handleCancel={this.handleCancel}
                                    />
                                </Popup>
                            </div>
                        </div>
                </div>
            ) 
        }
        else{
            return (
                <div>
                    <Navbar />
                        <div className='page_wrapper'>
                            <div className='page-container'>
                                <div className='smalltable-container'>
                                        <DefaultTable
                                            columns = {this.state.resultSetColumns} 
                                            rows={this.state.resultSetRows}
                                            searchColumns={this.state.searchColumns}
                                        />
                                    <div className='page-info'>
                                        <Button
                                            text={"Show Editor"}
                                            onClick={this.hideEditor}
                                        />
                                    </div>
                                </div>
                                <Popup 
                                    title={errorMessageTitle}
                                    openPopup={this.state.popupAcknowledgeOpen}
                                    setOpenPopup={this.setOpenPopupAcknowledge}>
                                    <AcknowledgeForm 
                                        message={this.state.backenderrormessage}
                                        handleacknowledge={this.handleacknowledge}
                                    />
                                </Popup>
                                <Popup 
                                    title={errorMessageTitle}
                                    openPopup={this.state.popupBackendWarning}
                                    setOpenPopup={this.setOpenPopupBackendWarning}>
                                    <OverridePermissionForm 
                                        errormessage={this.state.backenderrormessage}
                                        warningmessage={this.state.backendwarningmessage}
                                        handleOverride={this.handleOverride}
                                        handleCancel={this.handleCancel}
                                    />
                                </Popup>
                            </div>
                        </div>
                </div>
            ) 
        }
    }
}

export default CreateViewPage