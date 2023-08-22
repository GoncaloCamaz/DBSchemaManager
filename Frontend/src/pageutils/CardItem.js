import React from 'react'
import { Link } from 'react-router-dom'
import './Cards.css'

function CardItem(props) {
    return(
        <div className='cards__item'>
            <Link className ='cards__item__link' to={props.path}>
                <div className='cards__item__logo-wrap'>
                    <div className='cards__item__logo'>
                        <i className={props.src} ></i>
                    </div>
                </div>
                <div className='cards__item__info'>
                    <h1 className='cards__item__text'>{props.title}</h1>
                </div>
            </Link>
        </div>
    )
}

export default CardItem