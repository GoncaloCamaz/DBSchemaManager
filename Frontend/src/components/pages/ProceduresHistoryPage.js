import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import ViewHistoryTable from '../tables/ViewHistoryTable'
import Popup from '../../pageutils/Popup'
import ObjectForm from '../forms/ObjectForm'
import ConfirmForm from '../forms/ConfirmForm';
import ProcedureConsultForm from '../forms/ProcedureConsultForm'
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class ProceduresHistoryPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            scripts: [],
            recordForEdit: {},
            showOldVersionIcon: true,
            recordForExpand: {},
            popupSeeDetailsOpen: false,
            popupEditOpen: false,
            popupRemoveOpen: false,
            isLoaded: false,
            errorMessage: {},
            popupAcknowledgeOpen: false,
            currentSchema: '',
            schemas: [],
        }
    }
    
    componentDidMount() {
       this.proceduresUpdateTable(true)
    }

    proceduresUpdateTable(updateSchema) {
        this.setState({       
            showOldVersionIcon: true,
            isLoaded: false
        })
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        if(this.state.currentSchema === '' || this.state.currentSchema === 'All')
        {
            axios.get(backendURL+'/dbscript/latest/type/Procedure',authorization)
            .then(response => {
                this.setState({scripts: response.data})
            })
            .catch(error => {
                console.log(error)
            })
        }
        else
        {
            axios.get(backendURL+'/dbscript/latest/type/Procedure/dbschema/'+this.state.currentSchema,authorization)
            .then(response => {
                this.setState({scripts: response.data})
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

    setOpenSeeDetailsPopup = state => {
        this.setState({popupSeeDetailsOpen: state})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    editObject = (script, resetForm) => {
        if(script)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            script = {...script}

            var url = backendURL+"/dbscript/update"
                axios.post(url,script,requestparams)
                .then((response) => {
                    resetForm()
                    this.setState({recordForEdit: {}})
                    this.setOpenEditPopup(false)
                    this.proceduresUpdateTable(false)
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
     * Function to remove a given script
     * @param {*} script
     */
    removeScript = (script) => {
        if(script) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                },
                data: {
                    dbSchemaName: script.dbSchemaName,
                    dbObjectName: script.dbObjectName,
                    date: script.date,
                    code: script.code
                }    
            }

            var url = backendURL+"/dbscript/delete"
            axios.delete(url, requestparams)
            .then((response) => {
                this.setOpenPopupRemove(false)
                this.proceduresUpdateTable(false)
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

    handleViewDetails = item => {
        this.setState({recordForEdit: {script: item.code}}, () => {
            this.setOpenSeeDetailsPopup(true)
        })
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    handleViewOldRecords = (item) => {
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL+'/dbscript/dbschema/'+item.dbSchemaName + "/dbobject/"+item.dbObjectName,authorization)
        .then(response => {
            this.setState({scripts: response.data})
            this.setState({showOldVersionIcon: false})
        })
        .catch(error => {
            console.log(error)
        })

        this.setState({isLoaded: true})
    }

    handleReturn = () => {
        this.setState({showOldVersionIcon: true})
        this.proceduresUpdateTable(false)
    }

    handleSelectSchemaChange = item => {
        this.setState({
            currentSchema: item, 
        }, () => {
            this.proceduresUpdateTable(false)
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
                                    <ViewHistoryTable  
                                        rows={this.state.scripts} 
                                        openInPopup={this.openInPopup}
                                        showOldVersions={this.state.showOldVersionIcon}
                                        handleReturn = {this.handleReturn}
                                        handleViewOldRecords = {this.handleViewOldRecords}
                                        handleViewDetails={this.handleViewDetails}
                                        openInPopupRemove={this.openInPopupRemove}
                                        schemas={this.state.schemas}
                                        handleSelectSchemaChange={this.handleSelectSchemaChange}
                                    />
                                    <Popup 
                                        title={'Edit Script Description'}
                                        openPopup={this.state.popupEditOpen}
                                        setOpenPopup={this.setOpenEditPopup}>
                                        <ObjectForm 
                                            recordForEdit ={this.state.recordForEdit}
                                            addOrEdit={this.editObject}
                                        />
                                    </Popup>
                                    <Popup 
                                        title={'Procedure Script'}
                                        openPopup={this.state.popupSeeDetailsOpen}
                                        setOpenPopup={this.setOpenSeeDetailsPopup}>
                                        <ProcedureConsultForm
                                            recordForEdit={this.state.recordForEdit}
                                        />
                                    </Popup>
                                    <Popup 
                                        title={'Do you want to remove the Script?'}
                                        openPopup={this.state.popupRemoveOpen}
                                        setOpenPopup={this.setOpenPopupRemove}>
                                        <ConfirmForm 
                                            warningmessage={'This version will be removed on DBSchema Manager Database'}
                                            recordForRemove={this.state.recordForRemove}
                                            removeFunction={this.removeScript}
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

export default ProceduresHistoryPage