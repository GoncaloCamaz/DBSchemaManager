import React from 'react';
import '../../index.css';
import Cards from '../../pageutils/Cards';
import Navbar from '../navbar/Navbar'

export default function ManagementPage() {
    return (
        <div>
            <Navbar />
                <div className='management'>
                    <Cards page='management'/>
                </div>
        </div>
    );
}