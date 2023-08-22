import React, {useState,  useContext} from 'react'
import './Navbar.css'
import { Link } from 'react-router-dom'
import Dropdown from './Dropdown'
import { Button } from 'reactstrap'
import { Context } from '../../context/AuthContext';

function Navbar() {
    const [click, setClick] = useState(false);
    const [dropdown_schemas, setDropdownSchemas] = useState(false);
    const [dropdown_management, setDropdownManagement] = useState(false);
    const [dropdown_account, setDropdownAccount] = useState(false);
    const userRole = localStorage.getItem('userrole')
    const handleClick = () => setClick(!click);
    const closeMobileMenu = () => setClick(false);

    const { handleLogout } = useContext(Context);

    const onMouseEnter_Management = () => {
        if(window.innerWidth < 960)
        {
            setDropdownManagement(false)
        }
        else{
            setDropdownManagement(true)
        }
    };

    const onMouseLeave_Management = () => {
        if(window.innerWidth < 960)
        {
            setDropdownManagement(false)
        }
        else{
            setDropdownManagement(false)
        }
    };

    const onMouseEnter_Schemas = () => {
        if(window.innerWidth < 960)
        {
            setDropdownSchemas(false)
        }
        else{
            setDropdownSchemas(true)
        }
    };

    const onMouseLeave_Schemas = () => {
        if(window.innerWidth < 960)
        {
            setDropdownSchemas(false)
        }
        else{
            setDropdownSchemas(false)
        }
    };

    const onMouseEnter_Account = () => {
        if(window.innerWidth < 960)
        {
            setDropdownAccount(false)
        }
        else{
            setDropdownAccount(true)
        }
    };

    const onMouseLeave_Account = () => {
        if(window.innerWidth < 960)
        {
            setDropdownAccount(false)
        }
        else{
            setDropdownAccount(false)
        }
    };

    if(userRole === 'ADMIN')
    {
        return (
            <nav className='navbar'>
                <Link to='/home' className='navbar-logo'>
					DBSchema Manager 
                </Link>
                <div className='menu-icon' onClick={handleClick}>
                    <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
                </div>
                <div>
                <ul className={click ? 'nav-menu active' : 'nav-menu'}>
                    <li className='nav-item' onMouseEnter={onMouseEnter_Schemas}
                        onMouseLeave={onMouseLeave_Schemas}>
                        <Link to='/databases' className='nav-links' onClick={closeMobileMenu}>
                            Databases <i className='fas fa-caret-down'/>
                        </Link>
                        {dropdown_schemas && <Dropdown dropdownMenu='schemas'/>}
                    </li>
                    <li className='nav-item' onMouseEnter={onMouseEnter_Management}
                        onMouseLeave={onMouseLeave_Management}>
                        <Link to='/management' className='nav-links' onClick={closeMobileMenu}>
                            History & Management <i className='fas fa-caret-down'/>
                        </Link>
                        {dropdown_management && <Dropdown dropdownMenu='management'/>}
                    </li>
                    <li className='nav-item' onMouseEnter={onMouseEnter_Account}
                        onMouseLeave={onMouseLeave_Account}>
                        <Link to='/accounts' className='nav-links' onClick={closeMobileMenu}>
                            Accounts <i className='fas fa-caret-down'/>
                        </Link>
                        {dropdown_account && <Dropdown dropdownMenu='account'/>}
                    </li>
                    <li className='nav-item'>
                        <Button className='btn-lg btn-dark-block' onClick={handleLogout}>Logout</Button>
                    </li>
                </ul>
                </div>
            </nav>
        )
    }
    else if(userRole === 'USER')
    {
        return (
            <nav className='navbar'>
                <Link to='/home' className='navbar-logo'>
                    DBSchema Manager 
                </Link>
                <div className='menu-icon' onClick={handleClick}>
                    <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
                </div>
                <ul className={click ? 'nav-menu active' : 'nav-menu'}>
                    <li className='nav-item' onMouseEnter={onMouseEnter_Schemas}
                        onMouseLeave={onMouseLeave_Schemas}>
                        <Link to='/databases' className='nav-links' onClick={closeMobileMenu}>
                            Databases <i className='fas fa-caret-down'/>
                        </Link>
                        {dropdown_schemas && <Dropdown dropdownMenu='schemas'/>}
                    </li>
                    <li className='nav-item' onMouseEnter={onMouseEnter_Management}
                        onMouseLeave={onMouseLeave_Management}>
                        <Link to='/management' className='nav-links' onClick={closeMobileMenu}>
                            History & Management <i className='fas fa-caret-down'/>
                        </Link>
                        {dropdown_management && <Dropdown dropdownMenu='management'/>}
                    </li>
                    <li className='nav-item'>
                        <Button className='btn-lg btn-dark-block' onClick={handleLogout}>Logout</Button>
                    </li>
                </ul>
            </nav>
        )
    }
    else
    {
        return (
            <nav className='navbar'>
                <Link to='/home' className='navbar-logo'>
					DBSchema Manager
                </Link>
                <div className='menu-icon' onClick={handleClick}>
                    <i className={click ? 'fas fa-times' : 'fas fa-bars'} />
                </div>
                <ul className={click ? 'nav-menu active' : 'nav-menu'}>
                    <li className='nav-item' onMouseEnter={onMouseEnter_Schemas}
                        onMouseLeave={onMouseLeave_Schemas}>
                        <Link to='/databases' className='nav-links' onClick={closeMobileMenu}>
                            Databases <i className='fas fa-caret-down'/>
                        </Link>
                        {dropdown_schemas && <Dropdown dropdownMenu='schemas'/>}
                    </li>
                    <li className='nav-item'>
                        <Button className='btn-lg btn-dark-block' onClick={handleLogout}>Logout</Button>
                    </li>
                </ul>
            </nav>
        )
    }
}

export default Navbar;