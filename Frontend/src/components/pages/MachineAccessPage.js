import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import MachineAccessTable from '../tables/MachineAccessTable'
import Popup from '../../pageutils/Popup'
import MachineAccessForm from '../forms/MachineAccessForm'
import ConfirmForm from '../forms/ConfirmForm';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { Redirect } from 'react-router-dom';
import Navbar from '../navbar/Navbar'
import { backendURL } from '../../constants';

class MachineAccessPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            machines: [],
            currentMachineName: this.props.location.state.currentMachineName,
            currentMachineServiceip: this.props.location.state.currentMachineServiceip,
            recordForEdit: {},
            recordForRemove: {},
            errorMessage: {},
            isLoaded: false,
            popupAddOpen: false,
            popupEditOpen: false,
            popupRemoveOpen: false,
            popupAcknowledgeOpen: false,
            returnToMachines: false
        }
    }
    
    componentDidMount() {
        this.accessTableUpdate()
    }

    accessTableUpdate() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        axios.get(backendURL+'/machineaccess/machine/'
            +this.state.currentMachineName,authorization)
        .then(response => {
            this.setState({machines: response.data})
        })
        .catch(error => {
            console.log(error)
        })
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

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    setReturnToMachines = state => {
        this.setState({returnToMachines: state})
    }

    addAccess = (access, resetForm) => {

        if(access)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            access = {
                ...access,
                machinename: this.state.currentMachineName,
            }
            var url = backendURL+"/machineaccess/save"
                axios.post(url,access,requestparams)
                    .then((response) => {
                        if(response.data.statuscode === 409 || response.data.statuscode === 400)
                        {
                            this.setState({errorMessage: response.data.errormessage})
                            this.setOpenPopupAcknowledge(true)
                        }
                        else
                        {
                            resetForm()
                            this.setOpenAddPopup(false)
                            this.accessTableUpdate()
                        }
                    })
                    .catch(error => {
                        console.log(error)
                    })
        }
    }

    editAccess = (access, resetForm) => {
        if(access)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
 
            access = {
                ...access,
                machinename: this.state.currentMachineName,
            }
            var url = backendURL+"/machineaccess/update"
                axios.post(url,access,requestparams)
                    .then((response) => {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditPopup(false)
                        this.accessTableUpdate()
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
     * Function to remove a given Access
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

            var url = backendURL+"/machineaccess/delete/"
                + this.state.currentMachineName + "/"
                + access.username
            axios.delete(url, requestparams)
            .then((response) => {
                this.setOpenPopupRemove(false)
                this.accessTableUpdate()
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
    
    handleAddMachine = () => {
        this.setOpenAddPopup(true)
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    handleReturn = () => {
        this.setReturnToMachines(true)
    }

    render() {
        const { isLoaded, returnToMachines } = this.state;

        if(!isLoaded)
        {
            return (
                <div>
                    <Navbar />
                    <div className='page_wrapper'>
                        <h4 className='blinkText'>Loading..</h4>
                    </div>
                </div>
            )
        }
        else if(returnToMachines)
        {
            return <Redirect to="/organization/machines" />
        }
        else {
            return (
                <div>
                    <Navbar />
                    <div className='page_wrapper'>
                        <div className='page-container'>
                            <div className='table-container'>
                                <MachineAccessTable
                                    rows={this.state.machines} 
                                    openInPopup={this.openInPopup}
                                    openAddPopup={this.handleAddMachine}
                                    openInPopupRemove={this.openInPopupRemove}
                                    handleReturn={this.handleReturn}
                                />
                                <Popup 
                                    title={'Edit Access'}
                                    openPopup={this.state.popupEditOpen}
                                    setOpenPopup={this.setOpenEditPopup}>
                                    <MachineAccessForm 
                                        recordForEdit ={this.state.recordForEdit}
                                        addOrEdit={this.editAccess}
                                        add={false}
                                    />
                                </Popup>
                                <Popup 
                                    title={'Add New Access'}
                                    openPopup={this.state.popupAddOpen}
                                    setOpenPopup={this.setOpenAddPopup}>
                                    <MachineAccessForm 
                                        addOrEdit={this.addAccess}
                                        add={true}
                                    />
                                </Popup>
                                <Popup 
                                    title={'Do you want to remove the Access?'}
                                    openPopup={this.state.popupRemoveOpen}
                                    setOpenPopup={this.setOpenPopupRemove}>
                                    <ConfirmForm 
                                        recordForRemove={this.state.recordForRemove}
                                        removeFunction={this.removeAccess}
                                    />
                                </Popup>
                                <Popup 
                                    title='Something went wrong'
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

export default MachineAccessPage