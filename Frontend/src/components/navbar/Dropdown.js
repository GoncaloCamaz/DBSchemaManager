import React, {useState} from 'react';
import {Link} from 'react-router-dom';
import {MenuItems_Management, MenuItems_Organization, MenuItems_Schemas, MenuItems_Account} from './MenuItems';
import './Dropdown.css';

function Dropdown(props) {

    const [click, setClick] = useState(false);
    const handleClick = () => setClick(!click);
    
    if(props.dropdownMenu === 'organization')
    {
        return(
            <div>
                <ul onClick={handleClick}
                className={click ? 'dropdown-menu clicked' : 'dropdown-menu'}>
                    {MenuItems_Organization.map((item, index) => {
                        return(
                            <li key = {index}>
                                <Link className={item.cName} to={item.path} 
                                    onClick={() =>setClick(false)}>
                                        {item.title}
                                </Link>
                            </li>
                        )
                    })}
                </ul>
            </div>
         )
    }
    if (props.dropdownMenu === 'schemas')
    {
        return(
            <div>
                <ul onClick={handleClick}
                className={click ? 'dropdown-menu clicked' : 'dropdown-menu'}>
                    {MenuItems_Schemas.map((item, index) => {
                        return(
                            <li key = {index}>
                                <Link className={item.cName} to={item.path} 
                                    onClick={() =>setClick(false)}>                                        
                                        {item.title}
                                </Link>
                            </li>
                        )
                    })}
                </ul>
            </div>
         )
    }
    else if (props.dropdownMenu === 'management')
    {
        return(
            <div>
                <ul onClick={handleClick}
                className={click ? 'dropdown-menu clicked' : 'dropdown-menu'}>
                    {MenuItems_Management.map((item, index) => {
                        return(
                            <li key = {index}>
                                <Link className={item.cName} to={item.path} 
                                    onClick={() =>setClick(false)}>                                        
                                        {item.title}
                                </Link>
                            </li>
                        )
                    })}
                </ul>
            </div>
         )
    }
    else
    {
        return(
            <div>
                <ul onClick={handleClick}
                className={click ? 'dropdown-menu clicked' : 'dropdown-menu'}>
                    {MenuItems_Account.map((item, index) => {
                        return(
                            <li key = {index}>
                                <Link className={item.cName} to={item.path} 
                                    onClick={() =>setClick(false)}>                                        
                                        {item.title}
                                </Link>
                            </li>
                        )
                    })}
                </ul>
            </div>
         )
    }
}

export default Dropdown;