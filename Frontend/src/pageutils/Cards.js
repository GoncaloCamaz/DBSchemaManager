import React from 'react'
import CardItem from './CardItem'
import './Cards.css'
import { Cards_Databases, Cards_Management , Cards_Organization, Cards_Views, Cards_Accounts, Cards_Accesses } from './CardItemsInfo'

function Cards(props) { 

    if(props.page === 'organization')
    {
        return (
            <div className='cards'>
                <div className='cards__container'>
                    <div className='cards__wrapper'>
                        <ul className='cards__items'>
                            {Cards_Organization.map((item, index) => {
                                return(
                                    <li key = {index}>
                                        <CardItem path={item.path} src={item.src}
                                         title={item.title}/>
                                    </li>
                                )
                            })}
                        </ul>
                    </div>
                </div>
            </div>
        )
    }
    else if(props.page === 'databases')
    {
        return (
            <div className='cards'>
                <div className='cards__container'>
                    <div className='cards__wrapper'>
                        <ul className='cards__items'>
                            {Cards_Databases.map((item, index) => {
                                return(
                                    <li key = {index}>
                                        <CardItem path={item.path} src={item.src}
                                         title={item.title}/>
                                    </li>
                                )
                            })}
                         </ul>
                    </div>
                </div>
            </div>
        );
    }
    else if (props.page === 'management')
    {
        return (
            <div className='cards'>
                <div className='cards__container'>
                    <div className='cards__wrapper'>
                        <ul className='cards__items'>
                            {Cards_Management.map((item, index) => {
                                return(
                                    <li key =  {index}>
                                        <CardItem path={item.path} src={item.src}
                                            title={item.title}/>
                                    </li>
                                )
                            })}
                         </ul>
                    </div>
                </div>
            </div>
        );
    }
    else if (props.page === 'views')
    {
        return (
            <div className='cards'>
                <div className='cards__container'>
                    <div className='cards__wrapper'>
                        <ul className='cards__items'>
                            {Cards_Views.map((item, index) => {
                                return(
                                    <li key =  {index}>
                                        <CardItem path={item.path} src={item.src}
                                            title={item.title}/>
                                    </li>
                                )
                            })}
                         </ul>
                    </div>
                </div>
            </div>
        );
    }
    else if (props.page === 'accesses')
    {
        return (
            <div className='cards'>
                <div className='cards__container'>
                    <div className='cards__wrapper'>
                        <ul className='cards__items'>
                            {Cards_Accesses.map((item, index) => {
                                return(
                                    <li key =  {index}>
                                        <CardItem path={item.path} src={item.src}
                                            title={item.title}/>
                                    </li>
                                )
                            })}
                         </ul>
                    </div>
                </div>
            </div>
        );
    }
    else
    {
        return (
            <div className='cards'>
                <div className='cards__container'>
                    <div className='cards__wrapper'>
                        <ul className='cards__items'>
                            {Cards_Accounts.map((item, index) => {
                                return(
                                    <li key =  {index}>
                                        <CardItem path={item.path} src={item.src}
                                            title={item.title}/>
                                    </li>
                                )
                            })}
                         </ul>
                    </div>
                </div>
            </div>
        );
    }
}

export default Cards
