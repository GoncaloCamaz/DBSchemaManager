import React from 'react';
import '../../index.css';
import Cards from '../../pageutils/Cards';
import Navbar from '../navbar/Navbar'

export default function AccessPage() {
    return (
        <div>
            <Navbar />
            <div className='views'>
                <Cards page='accesses'/>
            </div>
        </div>
    );
}