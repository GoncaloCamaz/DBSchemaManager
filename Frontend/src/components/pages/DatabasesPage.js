import React from 'react';
import '../../index.css';
import Cards from '../../pageutils/Cards';
import Navbar from '../navbar/Navbar'

export default function DatabasesPage() {
    return (
        <div>
            <Navbar />
                <div className='databases'>
                    <Cards page='databases'/>
                </div> 
        </div>     
    );
}