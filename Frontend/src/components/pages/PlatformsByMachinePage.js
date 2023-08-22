import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import PlatformsByMachineTable from '../tables/PlatformsByMachineTable'
import Popup from '../../pageutils/Popup'
import PlatformsAvailableForm from '../forms/PlatformsAvailableForm'
import ConfirmForm from '../forms/ConfirmForm';
import { Redirect } from 'react-router-dom';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class PlatformsByMachinePage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            platforms: [],
            platformsAvailable: [],
            recordForRemove: {},
            currentMachineName: this.props.location.state.currentMachineName,
            currentMachineServiceip: this.props.location.state.currentMachineServiceip,
            isLoaded: false,
            popupAddOpen: false,
            popupRemoveOpen: false,
            returnToMachines: false,
            popupAcknowledgeOpen: false,
            errorMessage: '',
            acknowledgePopupTitle: ''
        }
    }
    
    componentDidMount() {
        this.platformByMachineTableUpdate()
    }

    platformByMachineTableUpdate()
    {
        this.setState({isLoaded: false})
        this.setState({platforms:[]})
        this.setState({platformsAvailable:[]})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }
        axios.get(backendURL+'/machineplatform/platforms/machine/'
        + this.state.currentMachineName,authorization)
        .then(response => {
            this.setState({platforms: response.data})
        })
        .catch(error => {
            console.log(error)
        })
        if(this.state.platforms !== [])
        {
            const url = backendURL+'/machineplatform/platforms/available/' 
                + this.state.currentMachineName
            axios.get(url,authorization)
                .then(response => {
                    response.data.forEach(e => 
                        this.state.platformsAvailable.push({value: e.name, id: e.name, label: e.name, title: e.name}))
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

    addMachinePlatform = (platform, resetForm) => {

        if(platform)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            const machinePlatform = {
                platformname: platform.name,
                machinename: this.state.currentMachineName,
            }
            var url = backendURL+"/machineplatform/save"
                axios.post(url,machinePlatform,requestparams)
                    .then((response) => {
                        if(response.data.statuscode === 201)
                        {
                            resetForm()
                            this.setOpenAddPopup(false)
                            this.platformByMachineTableUpdate()
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
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }
            }
            var url = backendURL+"/machineplatform/delete/"
                + this.state.currentMachineName + "/"
                + machinePlatform.name
            axios.delete(url, requestparams)
            .then((response) => {
                if(response.data.statuscode === 200)
                {
                    this.setOpenPopupRemove(false)
                    this.platformByMachineTableUpdate()
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
        this.setState({returnToMachines: true})
     }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    render() {
        const { isLoaded, returnToMachines } = this.state;

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
                                <PlatformsByMachineTable  
                                    rows={this.state.platforms} 
                                    openInPopup={this.setOpenAddPopup}
                                    openInPopupRemove={this.openInPopupRemove}
                                    returnToPage={this.handleReturn}
                                />
                                <Popup 
                                    title={'Add Relation'}
                                    openPopup={this.state.popupAddOpen}
                                    setOpenPopup={this.setOpenAddPopup}>
                                    <PlatformsAvailableForm
                                        platforms={this.state.platformsAvailable}
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

export default PlatformsByMachinePage