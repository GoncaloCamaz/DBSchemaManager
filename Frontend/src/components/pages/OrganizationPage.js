import React from 'react';
import '../../index.css';
import Cards from '../../pageutils/Cards';
import Navbar from '../navbar/Navbar'

export default function OrganizationPage() {
    return (
        <div>
            <Navbar />
                <div className='organization'>
                    <Cards page='organization'/>
                </div>
        </div>
    );
}