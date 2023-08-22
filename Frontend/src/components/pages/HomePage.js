import React from 'react';
import '../../index.css';
import Navbar from '../navbar/Navbar'

export default function HomePage() {
    const username = localStorage.getItem("username")

    return (
        <div>
            <Navbar />
                <div className='home'>
                    <div className='home_wrapper'>
                        <h1>DBSchema Manager</h1>
                        <h5>Welcome, {username}</h5>
                    </div>
                </div> 
        </div>   
    );
}