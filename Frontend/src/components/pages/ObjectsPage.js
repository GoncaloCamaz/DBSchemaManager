import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import ObjectsTable from '../tables/ObjectsTable'
import Popup from '../../pageutils/Popup'
import ObjectForm from '../forms/ObjectForm'
import ConfirmForm from '../forms/ConfirmForm';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class ObjectsPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            schemas: [],
            objects: [],
            currentSchema: '',
            recordForEdit: {},
            recordForRemove: {},
            isLoadingObjects: true,
            isLoaded: false,
            popupEditOpen: false,
            errorMessage: {},
            popupRemoveOpen: false,
            popupAcknowledgeOpen: false
        }
    }
    
    componentDidMount() {
        this.objectsTableUpdate(true)
    }

    objectsTableUpdate(updateSchema) {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        
        if(this.state.currentSchema === '' || this.state.currentSchema === 'All')
        {
            axios.get(backendURL+'/dbobject',authorization)
            .then(response => {
                this.setState({objects: response.data})
            })
            .catch(error => {
                console.log(error)
            })
        }
        else
        {
            axios.get(backendURL+'/dbobject/dbschema/' + this.state.currentSchema,authorization)
            .then(response => {
                this.setState({objects: response.data})
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

    editObject = (dbObject, resetForm) => {
        if(dbObject)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            dbObject = {...dbObject}

            var url = backendURL+"/dbobject/update"
                axios.post(url,dbObject,requestparams)
                .then((response) => {
                    resetForm()
                    this.setState({recordForEdit: {}})
                    this.setOpenEditPopup(false)
                    this.objectsTableUpdate(false)
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
     * Function to remove a given Object
     * @param {*} Object 
     */
    removeObject = (dbobject) => {
        if(dbobject) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }
            }

            var url = backendURL+"/dbobject/delete/"+dbobject.dbSchemaName+"/"+dbobject.name
            axios.delete(url, requestparams)
            .then((response) => {
                this.setOpenPopupRemove(false)
                this.objectsTableUpdate(false)
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
            this.objectsTableUpdate(false)
        })
    }

    render() {
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
        else {
             return (
                <div>
                    <Navbar />
                    <div className='page_wrapper'>
                        <div className='page-container'>
                            <div className='table-container'>
                                <ObjectsTable
                                    rows={this.state.objects} 
                                    openInPopup={this.openInPopup}
                                    openInPopupRemove={this.openInPopupRemove}
                                    schemas={schemas}
                                    handleSelectSchemaChange={this.handleSelectSchemaChange}
                                />
                                <Popup 
                                    title={'Edit Object'}
                                    openPopup={this.state.popupEditOpen}
                                    setOpenPopup={this.setOpenEditPopup}>
                                    <ObjectForm 
                                        recordForEdit ={this.state.recordForEdit}
                                        addOrEdit={this.editObject}
                                    />
                                </Popup>
                                <Popup 
                                    title={'Do you want to remove the Object?'}
                                    openPopup={this.state.popupRemoveOpen}
                                    setOpenPopup={this.setOpenPopupRemove}>
                                    <ConfirmForm 
                                        warningmessage={"Object " + this.state.recordForRemove.name
                                            + " will be deleted from DBSchema Manager database."}
                                        recordForRemove={this.state.recordForRemove}
                                        removeFunction={this.removeObject}
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

export default ObjectsPage