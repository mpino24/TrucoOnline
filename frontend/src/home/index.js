import React, { useState, useCallback, useEffect } from 'react';
import { Link } from 'react-router-dom';
import '../App.css';
import '../static/css/home/home.css';
import '../static/css/home/homeButton.css';
import { FaUserFriends } from "react-icons/fa";
import { RiArrowRightDoubleLine } from "react-icons/ri";
import { RiArrowLeftDoubleLine } from "react-icons/ri";
import { IoTrophy } from "react-icons/io5";
import tokenService from "../services/token.service.js";
import CreationModal from "../components/getCreationModal.js"
import { NavLink, NavItem, Nav } from 'reactstrap';
import GetJoinModal from '../components/getJoinModal.js';
import useFetchState from "../util/useFetchState.js";
import GetFriendsModal from '../components/getFriendsModal';


export default function Home() {
    const [joinModalView, setJoinModalView] = useState(false);
    const [creationModalView, setCreationModalView] = useState(false);
    const [friendsView, setFriendsView] = useState(false);
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const [backgroundUrl, setBackgroundUrl] = useState();
    const [username, setUsername] = useState("");
    const [photoUrl, setPhotoUrl] = useState('/fotoPerfil.jpg');




    const usuario = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);


    const [player, setPlayer] = useFetchState(
        [],
        '/api/v1/jugador?userId=' + usuario.id,
        jwt,
        setMessage,
        setVisible
    );


    const toggleAccountMenu = useCallback(() => {
        setShowAccountMenu((current) => !current);
    }, []);
    useEffect(() => {
        const backgrounds = [
            '/fondos/fondo0.jpg',
            '/fondos/fondo1.jpg',
            '/fondos/fondo2.jpg',
            '/fondos/fondo3.jpg',
            '/fondos/fondo4.jpg',
            '/fondos/fondo5.jpg',
            '/fondos/fondo6.jpg',
            '/fondos/fondo7.jpg',
            '/fondos/fondo8.jpg',
            '/fondos/fondo9.jpg',
        ];
        const randomIndex = Math.floor(Math.random() * backgrounds.length)
        setBackgroundUrl(backgrounds[randomIndex])
    }, []);





    const toggleJoinModal = useCallback(() => {
        const audio = new Audio("/duck.mp3");
        audio.play().catch((error) => {
            console.error('Error al reproducir el sonido:', error);
        });
        setJoinModalView((current) => !current);
    }, []);

    const toggleCreationModal = useCallback(() => {
        setCreationModalView((current) => !current);
    }, []);

    const toggleFriendsModal = useCallback(() => {
        setFriendsView((current) => !current);
    }, [])

    useEffect(() => {
        if (usuario) {
            setUsername(usuario.username);
            setPhotoUrl(player.photo)
        }
    }, [usuario, player])




    return (
        <>
            {!friendsView &&
                <Nav className="ms-auto mb-2 mb-lg-0" navbar style={{ float: 'right', marginTop: 15, marginRight: 15 }}>
                    <div style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }} onClick={toggleAccountMenu}>
                        <p style={{ color: "white", marginRight: 20, fontSize: 20 }} >{username}</p>
                        <img style={{ height: 60, width: 60, borderRadius: 500 }} src={photoUrl} alt='Foto de perfil del usuario'></img>
                    </div>

                    {showAccountMenu &&
                        <div style={{ backgroundColor: 'black', borderRadius: 5, height: 90, width: 150, float: 'right', marginRight: 5, marginTop: 5 }}>
                            <NavItem className="d-flex">
                                <NavLink style={{ color: "white", marginTop: 8, marginLeft: 5 }} id="perfil" tag={Link} to="/profile">Mi Perfil</NavLink>
                            </NavItem>

                            <NavItem className="d-flex">
                                <NavLink style={{ color: "red", marginBottom: 2, marginLeft: 5 }} id="logout" tag={Link} to="/logout">Cerrar Sesión</NavLink>
                            </NavItem>
                        </div>
                    }
                </Nav>
            }

            <div className="home-page-container" style={{ backgroundImage: `url(${backgroundUrl})`, backgroundSize: 'cover', backgroundPosition: 'center', backgroundRepeat: 'no-repeat', height: '100vh', width: '100vw' }}>
                <div style={{ width: '36%' }}>
                    <div style={{ position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)', cursor: 'pointer' }}>
                        <IoTrophy style={{ width: 80, height: 80, float: 'left', color: 'white' }} />
                        <RiArrowRightDoubleLine style={{ width: 80, height: 80, float: 'left', color: 'white' }} />
                        <div />
                    </div>
                </div>
                {joinModalView &&
                    <GetJoinModal
                        setModalVisible={setJoinModalView}
                        modalVisible={joinModalView} />
                }
                {creationModalView &&
                    <CreationModal setCreationModalView={setCreationModalView} creationModalView={creationModalView} />
                }
                {!(joinModalView || creationModalView) &&
                    <div className="hero-div">
                        <h1>¿Un truco?</h1>
                        <button className="home-button" onClick={toggleCreationModal}>Crear</button>
                        <button className="home-button" onClick={toggleJoinModal}>Unirte</button>
                    </div>
                }
                {!friendsView &&
                    <div style={{ width: '36%' }}>
                        <div style={{ position: 'absolute', right: 0, top: '50%', transform: 'translateY(-50%)', cursor: 'pointer' }} onClick={toggleFriendsModal} >
                            <FaUserFriends style={{ width: 80, height: 80, float: 'right', color: 'white' }} />
                            <RiArrowLeftDoubleLine style={{ width: 80, height: 80, float: 'right', color: 'white' }} />
                        </div>
                    </div>
                }
                {friendsView &&
                    <div style={{ width: '36%', height: '100%', marginRight: -12 }}>
                        <GetFriendsModal setModalVisible={setFriendsView} modalVisible={friendsView} />
                    </div>

                }
                <div style={{ backgroundColor: 'black', position: 'fixed', bottom: 0, width: '100%', height: 41 }}>
                    <center style={{ color: 'white', marginTop: 5 }}>© MIDPIE</center>
                </div>

            </div>
        </>
    );

}

