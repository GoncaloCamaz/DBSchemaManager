import React, {Component} from 'react';
import '../../index.css';
import './Pages.css'
import axios from 'axios'
import UsersTable from '../tables/UsersTable'
import Popup from '../../pageutils/Popup'
import UserForm from '../forms/UserForm'
import ConfirmForm from '../forms/ConfirmForm';
import AcknowledgeForm from '../forms/AcknowledgeForm'
import { backendURL } from '../../constants';
import Navbar from '../navbar/Navbar'

class UserAccountPage extends Component {
    constructor(props) {
        super(props)

        this.state = {
            users: [],
            recordForEdit: {},
            recordForRemove: {},
            isLoadingusers: true,
            isLoaded: false,
            popupAddOpen: false,
            popupRemoveOpen: false,
            popupEditOpen: false,
            errorMessage: '',
            popupAcknowledgeOpen: false
        }
    }
    
    componentDidMount() {
        this.usersTableUpdate()
    }

    usersTableUpdate() {
        this.setState({isLoaded: false})
        const authorization = {
            headers: {
                Authorization: localStorage.getItem('token')
            }
        }

        axios.get(backendURL+'/dbschemamanager/users',authorization)
        .then(response => {
            this.setState({users: response.data})
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

    addUser = (user, resetForm) => {
        if(user)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            user = {...user}

            var url = backendURL+"/dbschemamanager/register"
                axios.post(url,user,requestparams)
                .then((response => {
                    if(response.data.statuscode === 201)
                    {
                        resetForm()
                        this.setOpenAddPopup(false)
                        this.usersTableUpdate()
                    }
                    else
                    {
                        this.setState({errorMessage: response.data.errormessage})
                        this.setOpenPopupAcknowledge(true)
                        this.setState({isLoaded: true})
                    }
                }))
                .catch(error => {
                    this.setState({errorMessage: 'Something went wrong!'})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded:true})
                })
        }
    }

    editUser = (user, resetForm) => {
        if(user)
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token')
                }       
            }
            user = {...user}

            var url = backendURL+"/dbschemamanager/update"
                axios.post(url,user,requestparams)
                .then((response => {
                    if(response.data.statuscode === 200)
                    {
                        resetForm()
                        this.setState({recordForEdit: {}})
                        this.setOpenAddPopup(false)
                        this.usersTableUpdate()
                    }
                    else
                    {
                        this.setState({errorMessage: response.data.errormessage})
                        this.setOpenPopupAcknowledge(true)
                        this.setState({isLoaded: true})
                    }
                }))
                .catch(error => {
                    this.setState({errorMessage: 'Something went wrong!'})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded:true})
                })
        }
    }

    /**
     * Function to remove a given User
     * @param {*} user
     */
    removeUser = (user) => {
        if(user) 
        {
            const requestparams = {
                headers: {
                    Authorization: localStorage.getItem('token'),
                }    
            }

            var url = backendURL+"/dbschemamanager/delete/"+user.username
            axios.delete(url, requestparams)
            .then((response) => {
                if(response.data.statuscode === 200)
                {
                    this.setOpenPopupRemove(false)
                    this.usersTableUpdate()
                }
                else
                {
                    this.setState({errorMessage: response.data.errormessage})
                    this.setOpenPopupAcknowledge(true)
                    this.setState({isLoaded: true})
                }
            })
            .catch(error => {
                this.setState({errorMessage: 'Something went wrong!'})
                this.setOpenPopupAcknowledge(true)
                this.setState({isLoaded:true})
            })
        }
    }

    openInPopup = () => {
        this.setOpenAddPopup(true)
    }

    openEditPopup = item => {
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
                            <UsersTable 
                                rows={this.state.users} 
                                openInPopup={this.openInPopup}
                                openEditPopup={this.openEditPopup}
                                openInPopupRemove={this.openInPopupRemove}
                            />
                            <Popup 
                                title={'Add User'}
                                openPopup={this.state.popupAddOpen}
                                setOpenPopup={this.setOpenAddPopup}>
                                <UserForm 
                                    addOrEdit={this.addUser}
                                    add={true}
                                />
                            </Popup>
                            <Popup 
                                title={'Edit User'}
                                openPopup={this.state.popupEditOpen}
                                setOpenPopup={this.setOpenEditPopup}>
                                <UserForm 
                                    recordForEdit={this.state.recordForEdit}
                                    add={false}
                                    addOrEdit={this.editUser}
                                />
                            </Popup>
                            <Popup 
                                title={'Do you want to remove this user?'}
                                openPopup={this.state.popupRemoveOpen}
                                setOpenPopup={this.setOpenPopupRemove}>
                                <ConfirmForm 
                                    warningmessage={"User " + this.state.recordForRemove.username +" will no longer be able to login."}
                                    recordForRemove={this.state.recordForRemove}
                                    removeFunction={this.removeUser}
                                />
                            </Popup>
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
                </div>
            ) 
        }
    }
}

export default UserAccountPage