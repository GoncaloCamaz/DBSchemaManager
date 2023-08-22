import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import CreateViewResultTable from '../tables/CreateViewResultTable'
import SendQueryForm from '../forms/SendQueryForm'
import AcknowledgeForm from '../forms/AcknowledgeForm'
import Popup from '../../pageutils/Popup'
import DefaultTable from '../tables/DefaultTable'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'
import { Redirect } from 'react-router-dom';
import Button from '../controls/Button'

class SchemaQueryPage extends Component {
    constructor(props) {
        super(props)
        
        this.state = {
            schemaconnectionstring: '',
            rows: [],
            currentSchema: this.props.location.state.currentSchema,
            popupBackendWarning: false,
            returnToSchemas: false,
            backenderrormessage: '',
            backendwarningmessage: '',
            resultSetColumns: [],
            searchColumns: [],
            validQuery: false,
            resultSetRows: [],
            recordForEdit: {},
            query: {},
            isLoaded: false,
            errorMessage: '',
            hideEditor: false,
            popupAcknowledgeOpen: false,
            error: false
        }
    }
    
    componentDidMount() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        const schema = this.state.currentSchema
        if(schema !== null)
        {
            this.setState({schemaconnectionstring: schema})
            axios.get(backendURL+'/dbcolumn/dbschema/' + schema,authorization)
            .then(response => {
                this.setState({rows: response.data})
            })
            .catch(error => {
                console.log(error)
            })
        }
        else
        {
            this.setState({error: true})
        }
        this.setState({isLoaded: true})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    setOpenPopupBackendWarning = state => {
        this.setState({popupBackendWarning: state})
    }

    addScript = (script, _resetForm) => {
        if(script)
        {
            this.setState({isLoaded: false})
            const requestOptions = {
                method: 'POST',
                headers: { 'Content-type':'application/json', Authorization: localStorage.getItem('token')},
                body: JSON.stringify({schema: this.state.schemaconnectionstring, query: script.query})
            }
    
            this.setState({query: {query: script.query}})
            var url = backendURL+"/targetdatabase/query"
            fetch(url, requestOptions)
                .then(async response => {
                    const isJson = response.headers.get('content-type')?.includes('application/json');
                    const data = isJson && await response.json();
                    console.log(data)
                    this.handleResultSet(data)
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

    handleResultSet = (data) => {
        this.setState({resultSetColumns: []})
        this.setState({resultSetRows: []})
        this.setState({searchColumns: []})
        const columns = data.columns
        const rows = data.rows
        const errorMessage = data.errormessage
        const warningMessage = data.warningmessage
        
        if(data.validQuery === false)
        {
            this.setState({validQuery: false})
            if(errorMessage === '' && warningMessage === '')
            {
                this.setState({errorMessage: 'Introduced script might have errors!'})
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
            if(columns !== undefined && rows !== undefined)
            {
                for(var i = 0; i < columns.length; i++)
                {
                    this.state.resultSetColumns.push({id: columns[i], 
                        key: columns[i], 
                        label: columns[i]})
                    this.state.searchColumns.push({
                        id: columns[i],
                        key: columns[i],
                        title: columns[i],
                        label: columns[i],
                        value: columns[i]
                    })
                }
                for(var j = 0; j < rows.length; j++)
                {
                    this.state.resultSetRows.push(rows[j].row)
                }
    
                this.setState({validQuery: true})
            }
            else
            {
                this.setState({backenderrormessage: "Unable to receive full response!"})
                this.setOpenPopupAcknowledge(true)
            }
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

    handleReturnToSchemas = () => {
        this.setState({query: {}})
        this.setState({returnToSchemas: true})
    }

    hideEditor = () => {
        this.setState({hideEditor: !this.state.hideEditor})
    }

    render() {
        const { isLoaded, validQuery, returnToSchemas, hideEditor } = this.state;

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
        else if(returnToSchemas === true)
        {
            return <Redirect to="/databases/schemas" />
        }
        else if(validQuery === false) {
             return (
                <div>
                    <Navbar />
                        <div className='page_wrapper'>
                            <div className='page-container'>
                                <div className='smalltable-container'>
                                    <CreateViewResultTable 
                                            rows={this.state.rows}/>
                                    <div className='page-info'>
                                        <SendQueryForm
                                            returnToSchemas={this.handleReturnToSchemas}
                                            addOrEdit={this.addScript} 
                                            recordForEdit={this.state.query} 
                                        />
                                    </div>
                                </div>
                                <Popup 
                                    title={'Something went wrong'}
                                    openPopup={this.state.popupAcknowledgeOpen}
                                    setOpenPopup={this.setOpenPopupAcknowledge}>
                                    <AcknowledgeForm 
                                        message={this.state.backenderrormessage}
                                        handleacknowledge={this.handleacknowledge}
                                    />
                                </Popup>
                            </div>
                    </div>
                </div>
            ) 
        }
        else if(!hideEditor)
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
                                    <SendQueryForm
                                        returnToSchemas={this.handleReturnToSchemas}
                                        addOrEdit={this.addScript} 
                                        recordForEdit={this.state.query}
                                    />
                                    <Button
                                        text={"Hide Editor"}
                                        onClick={this.hideEditor}
                                    />
                                </div>
                            </div>
                            <Popup 
                                title={'Something went wrong!'}
                                openPopup={this.state.popupAcknowledgeOpen}
                                setOpenPopup={this.setOpenPopupAcknowledge}>
                                <AcknowledgeForm 
                                    message={this.state.backenderrormessage}
                                    handleacknowledge={this.handleacknowledge}
                                />
                            </Popup>
                        </div>
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
                                    title={'Something went wrong!'}
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
                ) 
        }
    }
}

export default SchemaQueryPage