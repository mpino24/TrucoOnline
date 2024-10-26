import React, { useState, useCallback, useEffect } from 'react';
import { Link, Router } from 'react-router-dom';
import '../App.css';
import '../static/css/home/home.css';
import '../static/css/home/homeButton.css';
import { FaUserFriends } from "react-icons/fa";
import { RiArrowRightDoubleLine } from "react-icons/ri";
import { RiArrowLeftDoubleLine } from "react-icons/ri";
import { IoTrophy } from "react-icons/io5";
import tokenService from "frontend/src/services/token.service.js";
import jwt_decode from "jwt-decode";
import CreationModal from "frontend/src/components/getCreationModal.js"
import { Navbar, NavbarBrand, NavLink, NavItem, Nav, NavbarText, NavbarToggler, Collapse } from 'reactstrap';
import GetJoinModal from 'frontend/src/components/getJoinModal.js';


export default function Home() {
    const [joinModalView, setJoinModalView] = useState(false);
    const [creationModalView, setCreationModalView] = useState(false);
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const [backgroundUrl, setBackgroundUrl] = useState()
    const [username, setUsername] = useState("");
    const [photoUrl,setPhotoUrl] = useState('/fotoPerfil.jpg')




    const jwt = tokenService.getLocalAccessToken();

    const toggleAccountMenu = useCallback(() => {
        setShowAccountMenu((current) => !current);
    }, []);
    useEffect(() => {
        const backgrounds = [
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
        const randomIndex = Math.floor(Math.random() * backgrounds.length)
        setBackgroundUrl(backgrounds[randomIndex])
    }, []);





    const toggleJoinModal = useCallback(() => {
        setJoinModalView((current) => !current);
    }, []);
  
      const toggleCreationModal = useCallback(() => {
        setCreationModalView((current) => !current);
    }, []);
  
    useEffect(() => {
        if (jwt) {
            setUsername(jwt_decode(jwt).sub);
        }
    }, [jwt])



    return (
        <>
    
        <Nav className="ms-auto mb-2 mb-lg-0" navbar style={{float: 'right', marginTop:15, marginRight:15}}>
            <div style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={toggleAccountMenu}>
                <p style={{ color: "white", marginRight: 20, fontSize: 20 }} >{username}</p>
                <img style={{ height: 60, width: 60, borderRadius: 500 }} src={photoUrl} alt='Foto de perfil del usuario'></img>
            </div>
        
        
            {showAccountMenu &&
        <div style={{ backgroundColor: 'black', borderRadius: 5, height: 90, width: 150, float: 'right', marginRight:5, marginTop:5 }}>
            <NavItem className="d-flex">
                <NavLink style={{ color: "white", marginTop: 8, marginLeft: 5 }} id="perfil" tag={Link} to="/profile">Mi Perfil</NavLink>
            </NavItem>

            <NavItem className="d-flex">
                <NavLink style={{ color: "red", marginBottom: 2, marginLeft: 5 }} id="logout" tag={Link} to="/logout">Cerrar Sesión</NavLink>
            </NavItem>
        </div>
        }
        </Nav>
        

    <div className="home-page-container" style={{ background: backgroundUrl, backgroundSize: '100%' }}>
        <div style={{ width: '36%' }}>
            <div style={{ cursor: 'pointer' }}>
                <IoTrophy style={{ width: 80, height: 80, float: 'left', color: 'white' }} />
                <RiArrowRightDoubleLine style={{ width: 80, height: 80, float: 'left', color: 'white' }} />
                <div />
            </div>
        </div>
        {joinModalView &&
            <GetJoinModal 
            setModalVisible={setJoinModalView}
            modalVisible={joinModalView}/>
        //    JoinModal(setJoinModalView, joinModalView,setCodigo,codigo,setMessage,message)         
        }
        {creationModalView &&
            CreationModal(setCreationModalView, creationModalView)         
        }
        {!(joinModalView || creationModalView) &&
            <div className="hero-div">
                <h1>¿Un truco?</h1>
                <button className="home-button" onClick={toggleCreationModal}>Crear</button>
                <button className="home-button" onClick={toggleJoinModal}>Unirte</button>
            </div>
        }
        <div style={{ width: '36%' }}>
            <div style={{ cursor: 'pointer' }} >
                <FaUserFriends style={{ width: 80, height: 80, float: 'right', color: 'white' }} />
                <RiArrowLeftDoubleLine style={{ width: 80, height: 80, float: 'right', color: 'white' }} />
            </div>
        </div>
        <div style={{ backgroundColor: 'black', position: 'fixed', bottom: 0, width: '100%', height: 41 }}>
            <center style={{ color: 'white', marginTop: 5 }}>© MIDPIE</center>
        </div>

    </div>
    </>
    );

}

