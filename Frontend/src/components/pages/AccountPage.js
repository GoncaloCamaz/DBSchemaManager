import React from 'react';
import '../../index.css';
import Navbar from '../navbar/Navbar'
import Cards from '../../pageutils/Cards';

export default function AccountPage() {
    return (
        <div>
            <Navbar />
                <div className='accounts'>
                    <Cards page='accounts'/>
                </div>   
        </div>  
    );
}