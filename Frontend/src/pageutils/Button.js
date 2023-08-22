import React from 'react'
import './Button.css'
import {Link} from 'react-router-dom';

function Button(props) {
    return(
        <Link to={props.linkTo}>
            <button className='btn'>{props.buttonText}</button>
        </Link>
    )
}

export default Button