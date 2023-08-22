import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import PlatformsTable from '../tables/PlatformsTable'
import Popup from '../../pageutils/Popup'
import PlatformForm from '../forms/PlatformForm'
import ConfirmForm from '../forms/ConfirmForm';
import { Redirect } from 'react-router-dom';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class PlatformPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            platforms: [],
            recordForEdit: {},
            recordForRemove: {},
            isLoaded: false,
            popupAddOpen: false,
            acknowledgePopupTitle: '',
            popupEditOpen: false,
            popupRemoveOpen: false,
            showCredentials: false,
            viewDetails: false,
            viewDetailsRow: {},
            errorMessage: '',
            popupAcknowledgeOpen: false
        }
    }

    componentDidMount() {
        this.platformsTableUpdate()
    }

    platformsTableUpdate()
    {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL+'/platform',authorization)
        .then(response => {
            this.setState({platforms: response.data})
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

    setViewDetails = state => {
        this.setState({viewDetails: state})
    }

    setOpenPopupAcknowledge = state => {
        this.setState({popupAcknowledgeOpen: state})
    }

    addPlatform = (platform, resetForm) => {
        if(platform)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            platform = {...platform}
            var url = backendURL+"/platform/save"
                axios.post(url,platform,requestparams)
                    .then((response) => {
                        if(response.data.statuscode === 201)
                        {
                            resetForm()
                            this.setState({recordForEdit: {}})
                            this.setOpenAddPopup(false)
                            this.platformsTableUpdate()
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

    editPlatform = (platform, resetForm) => {
        if(platform)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
 
            const updatedplatform = {
                name: platform.name, 
                url: platform.url, 
                description: platform.description 
            }
            var url = backendURL+"/platform/update"
            axios.post(url,updatedplatform,requestparams)
                .then((response) => {
                    if(response.data.statuscode === 200)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditPopup(false)
                        this.platformsTableUpdate()
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

    editCredentials = (platform, resetForm) => {
        if(platform)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
 
            platform = {...platform}
            var url = backendURL+"/platform/update/credentials"
                axios.post(url,platform,requestparams)
                .then((response) => {
                    if(response.data.statuscode === 200)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenEditPopup(false)
                        this.platformsTableUpdate()
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
     * @param {*} platform
     */
    removePlatform = (platform) => {
        if(platform) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }  
            }

            var url = backendURL+"/platform/delete/"+platform.name
            axios.delete(url, requestparams)
                .then((response) => {
                    if(response.data.statuscode === 200)
                    {
                        this.setState({recordForEdit: {}})
                        this.setOpenPopupRemove(false)
                        this.platformsTableUpdate()
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

    openInPopup = item => {
        this.setState({showCredentials: false})
        this.setState({recordForEdit: item})
        this.setOpenEditPopup(true)
    }

    openInPopupRemove = item => {
        this.setState({recordForRemove: item})
        this.setOpenPopupRemove(true)
    }
    
    handleAddPlatform = () => {
        this.setOpenAddPopup(true)
    }

    handleViewDetails = row => {
        this.setViewDetails(true)
        this.setState({viewDetailsRow: row})
    }

    handleViewCredentials = row => {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL+'/platform/credentials/name/'+row.name,authorization)
        .then(response => {
            this.setState({recordForEdit: response.data})
        })
        .catch(error => {
            console.log(error)
        })
        this.setState({isLoaded: true})
        this.setState({showCredentials: true})
        this.setOpenEditPopup(true)
    }

    handleacknowledge = () => {
        this.setOpenPopupAcknowledge(false)
    }

    render() {
        const { isLoaded, viewDetails, viewDetailsRow } = this.state;

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
        else if(viewDetails)
        {
            return <Redirect to={{
                pathname: "/organization/platforms/machines",
                state: {currentPlatformURL: viewDetailsRow.url, currentPlatformName: viewDetailsRow.name}
            }}/>
        }
        else {
             return (
                <div>
                    <Navbar />
                    <div className='page_wrapper'>
                        <div className='page-container'>
                            <div className='table-container'>
                                <PlatformsTable 
                                    rows={this.state.platforms} 
                                    handleViewDetails={this.handleViewDetails}
                                    handleViewCredentials={this.handleViewCredentials}
                                    openInPopup={this.openInPopup}
                                    openAddPopup={this.handleAddPlatform}
                                    openInPopupRemove={this.openInPopupRemove}
                                />
                                <Popup 
                                    title={'Edit Platform'}
                                    openPopup={this.state.popupEditOpen}
                                    setOpenPopup={this.setOpenEditPopup}>
                                    <PlatformForm 
                                        add={false}
                                        recordForEdit ={this.state.recordForEdit}
                                        showCredentials={this.state.showCredentials}
                                        addOrEdit={this.editPlatform}
                                        updateCredentials={this.editCredentials}
                                    />
                                </Popup>
                                <Popup 
                                    title={'Add New Platform'}
                                    openPopup={this.state.popupAddOpen}
                                    setOpenPopup={this.setOpenAddPopup}>
                                    <PlatformForm 
                                        add={true}
                                        addOrEdit={this.addPlatform}
                                    />
                                </Popup>
                                <Popup 
                                    title={'Do you want to remove the Platform?'}
                                    openPopup={this.state.popupRemoveOpen}
                                    setOpenPopup={this.setOpenPopupRemove}>
                                    <ConfirmForm 
                                        warningmessage={'Please make sure there are no relations regarding this platform ' 
                                            + this.state.recordForRemove.name + " ."}
                                        recordForRemove={this.state.recordForRemove}
                                        removeFunction={this.removePlatform}
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

export default PlatformPage