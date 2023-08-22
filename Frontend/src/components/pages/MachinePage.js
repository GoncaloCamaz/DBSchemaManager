import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import MachinesTable from '../tables/MachinesTable'
import Popup from '../../pageutils/Popup'
import MachineForm from '../forms/MachinesForm'
import ConfirmForm from '../forms/ConfirmForm';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { Redirect } from 'react-router-dom';
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class MachinePage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            machines: [],
            recordForEdit: {},
            recordForRemove: {},
            errorMessage: '',
            currentMachineName: '',
            acknowledgePopupTitle: '',
            isLoaded: false,
            popupAddOpen: false,
            popupEditOpen: false,
            popupRemoveOpen: false,
            popupAcknowledgeOpen: false,
            showAccessList: false,
            showAssociatedPlatforms: false
        }
    }
    
    componentDidMount() {
        this.machinesTableUpdate()
    }

    machinesTableUpdate() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL + '/machine',authorization)
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

    addMachine = (machine, resetForm) => {

        if(machine)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            machine = {...machine}
            var url = backendURL+"/machine/save"
                axios.post(url,machine,requestparams)
                    .then((response) => {
                        if(response.data.statuscode === 201)
                        {
                            resetForm()
                            this.setState({recordForEdit: {}})
                            this.setOpenAddPopup(false)
                            this.machinesTableUpdate()
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

    editMachine = (machine, resetForm) => {
        if(machine)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
 
            machine = {...machine}

            var url = backendURL+"/machine/update"
                axios.post(url,machine,requestparams)
                .then((response) => {
                    if(response.data.statuscode === 200)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditPopup(false)
                        this.machinesTableUpdate()
                    }
                    else{
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

    /**
     * Function to remove a given platform
     * @param {*} machine
     */
    removeMachine = (machine) => {
        if(machine) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }
            }

            var url = backendURL+"/machine/delete/"+machine.name
            axios.delete(url, requestparams)
            .then((response) => {
                if(response.data.statuscode === 200)
                {
                    this.setState({recordForEdit: {}})
                    this.setOpenPopupRemove(false)
                    this.machinesTableUpdate()
                }
                else{
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

    openAssociatedAccounts = item => {
        this.setState({currentMachineName: item.name})
        this.setState({currentMachineServiceip: item.serviceip})
        this.setState({showAccessList: true})
    }

    openPlatformsByMachine = item => {
        this.setState({currentMachineName: item.name})
        this.setState({currentMachineServiceip: item.serviceip})
        this.setState({showAssociatedPlatforms: true})
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

    render() {
        const { isLoaded, showAccessList, showAssociatedPlatforms } = this.state;

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
        else if(showAssociatedPlatforms)
        {
            return <Redirect to={{
                pathname: "machines/platforms",
                state: {currentMachineName: this.state.currentMachineName,
                        currentMachineServiceip: this.state.currentMachineServiceip
                }
            }}/>
        }
        else if(showAccessList)
        {
            return <Redirect to={{
                pathname: "machines/access",
                state: {currentMachineName: this.state.currentMachineName,
                        currentMachineServiceip: this.state.currentMachineServiceip
                }
            }}/>
        }
        else {
             return (
                <div>
                <Navbar />
                <div className='page_wrapper'>
                    <div className='page-container'>
                        <div className='table-container'>
                            <MachinesTable
                                rows={this.state.machines}
                                openAssociatedAccounts={this.openAssociatedAccounts}
                                openPlatformsByMachine={this.openPlatformsByMachine} 
                                openInPopup={this.openInPopup}
                                openAddPopup={this.handleAddMachine}
                                openInPopupRemove={this.openInPopupRemove}/>
                            <Popup 
                                title={'Edit Machine'}
                                openPopup={this.state.popupEditOpen}
                                setOpenPopup={this.setOpenEditPopup}>
                                <MachineForm 
                                    recordForEdit ={this.state.recordForEdit}
                                    addOrEdit={this.editMachine}
                                    add={false}
                                />
                            </Popup>
                            <Popup 
                                title={'Add New Machine'}
                                openPopup={this.state.popupAddOpen}
                                setOpenPopup={this.setOpenAddPopup}>
                                <MachineForm 
                                    addOrEdit={this.addMachine}
                                    add={true}
                                />
                            </Popup>
                            <Popup 
                                title={'Do you want to remove this Machine?'}
                                openPopup={this.state.popupRemoveOpen}
                                setOpenPopup={this.setOpenPopupRemove}>
                                <ConfirmForm 
                                    warningmessage={"Please make sure there are no relations regarding " 
                                    + this.state.recordForRemove.name + "."}
                                    recordForRemove={this.state.recordForRemove}
                                    removeFunction={this.removeMachine}
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

export default MachinePage