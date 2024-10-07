import React, { useState, useCallback } from 'react';
import '../App.css';
import '../static/css/home/home.css';
import '../static/css/home/homeButton.css';
import { FaUserFriends } from "react-icons/fa";
import { RiArrowRightDoubleLine } from "react-icons/ri";
import { RiArrowLeftDoubleLine } from "react-icons/ri";
import { IoTrophy } from "react-icons/io5";
import JoinModal from "frontend/src/components/getJoinModal.js"



export default function Home() {
    const [joinModalView, setJoinModalView]= useState(false);
    const backgrounds= [
        'url(/fondos/fondo0.jpg)',
        'url(/fondos/fondo1.jpg)',
        'url(/fondos/fondo2.jpg)',
        'url(/fondos/fondo3.jpg)',
        'url(/fondos/fondo4.jpg)',
        'url(/fondos/fondo5.jpg)',
        'url(/fondos/fondo6.jpg)',
        'url(/fondos/fondo7.jpg)',
        'url(/fondos/fondo8.jpg)',
        'url(/fondos/fondo9.jpg)',
    ];

    const getRandomBackground = () => { //Generado por IA
        const randomIndex = Math.floor(Math.random() * backgrounds.length);
        return backgrounds[randomIndex];
    }

    const toggleJoinModal = useCallback(() => {
        setJoinModalView((current) => !current);
    }, []);
       


    return (
        <div className="home-page-container" style={{background: getRandomBackground(), backgroundSize:'cover'}}>
            <div style={{width:'36%' }}>
                <div style={{cursor:'pointer'}}>
                <IoTrophy style={{ width: 80, height: 80, float: 'left',color:'white' }} />
                <RiArrowRightDoubleLine style={{ width: 80, height: 80, float: 'left',color:'white' }} />
                <div/>
            </div>
            </div>
            {joinModalView && 
            JoinModal(setJoinModalView, joinModalView)
                
            }
            { !joinModalView &&
            <div className="hero-div">
                <h1>¿Un truco?</h1>
                <button className="home-button">Crear</button>
                <button className="home-button" onClick={toggleJoinModal}>Unirte</button> 
            </div>
            }
            <div style={{ width:'36%' }}>
                <div style={{cursor:'pointer'}} >
                <FaUserFriends style={{ width: 80, height: 80, float: 'right',color:'white' }} />
                <RiArrowLeftDoubleLine style={{ width: 80, height: 80, float: 'right',color:'white' }} />
                </div>
            </div>

            <div style={{ backgroundColor: 'black', position: 'fixed', bottom: 0, width: '100%', height: 41 }}>
                <center style={{ color: 'white', marginTop: 5 }}>© MIDPIE</center>
            </div>

        </div>
    );

}

