import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import MachinesByPlatformTable from '../tables/MachinesByPlatformTable'
import Popup from '../../pageutils/Popup'
import MachinesAvailableForm from '../forms/MachinesAvailableForm'
import ConfirmForm from '../forms/ConfirmForm';
import { Redirect } from 'react-router-dom';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class MachinesByPlatformPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            machines: [],
            machineAvailable: [],
            recordForRemove: {},
            currentPlatformURL: this.props.location.state.currentPlatformURL,
            currentPlatformName: this.props.location.state.currentPlatformName,
            isLoaded: false,
            acknowledgePopupTitle: '',
            popupAddOpen: false,
            popupRemoveOpen: false,
            returnToPlatforms: false,
            popupAcknowledgeOpen: false,
            errorMessage: ''
        }
    }
    
    componentDidMount() {
        this.machinesByPlatformTableUpdate()
    }

    machinesByPlatformTableUpdate() 
    {
        this.setState({machines:[]})
        this.setState({machineAvailable:[]})
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL+'/machineplatform/machines/platform/'
        + this.state.currentPlatformName,authorization)
        .then(response => {
            this.setState({machines: response.data})
        })
        .catch(error => {
            console.log(error)
        })
        if(this.state.machines !== [])
        {
            const url = backendURL+'/machineplatform/machines/available/' 
                + this.state.currentPlatformName

            axios.get(url,authorization)
                .then(response => {
                    response.data.forEach(e => 
                        this.state.machineAvailable.push({value: e.name, id: e.name, label: e.name, title: e.name}))
                })
                .catch(error => {
                    console.log(error)
                })
        }
        this.setState({isLoaded: true})
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

    addMachinePlatform = (machinePlatform, resetForm) => {

        if(machinePlatform)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            machinePlatform = {
                machinename: machinePlatform.machine, 
                platformname: this.state.currentPlatformName
            }
            var url = backendURL+"/machineplatform/save"
                axios.post(url,machinePlatform,requestparams)
                    .then((response) => {
                        if(response.data.statuscode === 201)
                        {
                            resetForm()
                            this.setOpenAddPopup(false)
                            this.machinesByPlatformTableUpdate()
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

    /**
     * Function to remove a given machinePlatform
     * @param {*} machinePlatform
     */
    removeMachinePlatform = (machinePlatform) => {
        if(machinePlatform) 
        {
            const platformName = this.state.currentPlatformName
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }  
            }
            var url = backendURL+"/machineplatform/delete/"
                + machinePlatform.machinename + "/"
                + platformName
            axios.delete(url, requestparams)
            .then((response) => {
                if(response.data.statuscode === 200)
                {
                    this.setOpenPopupRemove(false)
                    this.machinesByPlatformTableUpdate()
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

    openInPopupRemove = item => {
        const todelete = item
        this.setState({recordForRemove: todelete})
        this.setOpenPopupRemove(true)
    }
    
    handleAddMachinePlatform = () => {
        this.setOpenAddPopup(true)
    }

    handleReturn = () => {
        this.setState({returnToPlatforms: true})
     }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    render() {
        const { isLoaded, returnToPlatforms } = this.state;

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
        else if(returnToPlatforms)
        {
            return <Redirect to="/organization/platforms" />
        }
        else {
             return (
                <div>
                    <Navbar />
                    <div className='page_wrapper'>
                        <div className='page-container'>
                            <div className='table-container'>
                                <MachinesByPlatformTable  
                                    rows={this.state.machines} 
                                    openInPopup={this.setOpenAddPopup}
                                    openInPopupRemove={this.openInPopupRemove}
                                    returnToPage={this.handleReturn}
                                />
                                <Popup 
                                    title={'Add Relation'}
                                    openPopup={this.state.popupAddOpen}
                                    setOpenPopup={this.setOpenAddPopup}>
                                    <MachinesAvailableForm
                                        machines={this.state.machineAvailable}
                                        addOrEdit={this.addMachinePlatform}
                                    />
                                </Popup>
                                <Popup 
                                    title={'Do you want to remove this relation?'}
                                    openPopup={this.state.popupRemoveOpen}
                                    setOpenPopup={this.setOpenPopupRemove}>
                                    <ConfirmForm 
                                        recordForRemove={this.state.recordForRemove}
                                        removeFunction={this.removeMachinePlatform}
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

export default MachinesByPlatformPage