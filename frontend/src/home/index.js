import React, { useState, useCallback, useEffect,useRef } from 'react';
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
import EstadisticasModal from '../estadisticas/EstadisticasModal';
import jwt_decode from "jwt-decode";
import { useNavigate } from 'react-router-dom';

export default function Home() {
    const [joinModalView, setJoinModalView] = useState(false);
    const [creationModalView, setCreationModalView] = useState(false);
    const [friendsView, setFriendsView] = useState(false);
    const friendViewRef = useRef(friendsView);
    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const [backgroundUrl, setBackgroundUrl] = useState();
    const [username, setUsername] = useState("");
    const [photoUrl, setPhotoUrl] = useState('/fotoPerfil.jpg');
    const [estadisticasView, setEstadisticasView] = useState(false);
    const [roles, setRoles] = useState([]);
    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [numMessages, setNumMessages] = useState(0);
    const numMessagesRef = useRef(numMessages);
    const usuario = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();
    const navigate = useNavigate();

    const [player, setPlayer] = useFetchState(
        [],
        '/api/v1/jugador?userId=' + usuario.id,
        jwt,
        setMessage,
        setVisible
    );

    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
        }
    }, [jwt])


    const toggleAccountMenu = useCallback(() => {
        setShowAccountMenu((current) => !current);
    }, []);

    function fetchCurrentGame() { //Redirección automática a la partida a la que este conectado el usuario
        fetch(
            "/api/v1/partidajugador/partidaJugadorActual",
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            }
        )
            .then((response) => {
                if (response.status === 202) {
                    return null;
                }
                return response.json();
            })
            .then((data) => {
                if (data !== null) {
                    if (data.game && data.game.estado !== "FINALIZADA") {
                        navigate("/partidas?partidaCode=" + data.game.codigo);
                    }
                }
            })
            .catch((message) => alert(message));
    }

    function fetchNotReadMessages() {
        fetch(
            "/api/v1/chat/unread",
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                },
            }
        )
            .then((response) => response.json())
            .then((data) => {
                if (data > 0 && numMessagesRef.current < data) {
                    if (!friendViewRef.current) {
                        const audio = new Audio("/notification.mp3");
                        audio.play().catch((error) => {
                            console.error('Error al reproducir el sonido:', error);
                        });
                    } 
                }
                setNumMessages(data);
            })
            .catch((message) => alert(message));
    }

    useEffect(() => {
        numMessagesRef.current = numMessages;
    }, [numMessages]);

    useEffect(() => {
        friendViewRef.current = friendsView;
    }, [friendsView]);

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

        fetchCurrentGame();
        fetchNotReadMessages();
        const timer = setInterval(() => {
            fetchNotReadMessages();
        }, 2000);
        return () => clearInterval(timer);

    }, []);

    useEffect(() => {
        if(!friendsView){
        fetchNotReadMessages();
        }
    }, [friendsView, numMessages]);





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


    const handleRedirect = (path) => {
        navigate(path);
    };

    const toggleEstadisticasModal = useCallback(() => {
        setEstadisticasView((current) => !current);
    }, [])




    return (
        <>  
            
                    
            {!estadisticasView && (
                <div expand='md' style={{ float: 'left' }}>
                    {roles.includes('ADMIN') && 
                    <button className="button-admin" onClick={() => { navigate("/admin") }}>
                        ADMINISTRACIÓN
                    </button>}
                    </div>
            )}
            
            {!estadisticasView && !friendsView &&
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
                {!estadisticasView &&
                    <>
                        <div style={{ width: '36%' }}>
                            <div style={{ position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)', cursor: 'pointer' }} onClick={toggleEstadisticasModal}>
                                <IoTrophy style={{ width: 80, height: 80, float: 'left', color: 'white' }} />
                                <RiArrowRightDoubleLine style={{ width: 80, height: 80, float: 'left', color: 'white' }} />
                                <div />
                            </div>
                        </div>
                        {joinModalView &&
                            <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000, width: '60%' }}>
                                <GetJoinModal
                                    setModalVisible={setJoinModalView}
                                    modalVisible={joinModalView} />
                            </div>
                        }
                        {creationModalView &&
                            <div style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', zIndex: 1000 }}>
                                <CreationModal setCreationModalView={setCreationModalView} creationModalView={creationModalView} />
                            </div>
                        }
                        {!(joinModalView || creationModalView) &&
                            <div className="hero-div" style={{ position: 'fixed', top: '50%', left: '50%', transform: 'translate(-50%, -50%)', textAlign: 'center' }}>
                                <h1>¿Un truco?</h1>
                                <button className="home-button" onClick={toggleCreationModal}>Crear</button>
                                <button className="home-button" onClick={toggleJoinModal}>Unirte</button>
                            </div>
                        }
                        {!friendsView &&
                            <div style={{ width: '36%' }}>
                                <div key={numMessages} style={{ position: 'absolute', right: 0, top: '50%', transform: 'translateY(-50%)', cursor: 'pointer' }} onClick={toggleFriendsModal} >
                                    <FaUserFriends style={{ width: 80, height: 80, float: 'right', color: 'white' }} />
                                    <RiArrowLeftDoubleLine style={{ width: 80, height: 80, float: 'right', color: 'white' }} />
                                    {numMessages > 0 && (
                                        <span style={{
                                            color: 'white',
                                            backgroundColor: 'orange',
                                            borderRadius: '50%',
                                            position: 'absolute',
                                            top: '-10px',
                                            right: '0px',
                                            width: '30px',
                                            height: '30px',
                                            display: 'flex',
                                            alignItems: 'center',
                                            justifyContent: 'center',
                                            fontSize: '20px'
                                        }}>
                                            {numMessages}
                                        </span>
                                    )}
                                </div>
                            </div>
                        }
                        {friendsView &&
                            <div style={{ position: 'fixed', right: 0, top: 0, height: '100%', width: '36%' }}>
                                <GetFriendsModal setModalVisible={setFriendsView} modalVisible={friendsView} />
                            </div>

                        }
                    </>
                }
                {estadisticasView &&
                    <div style={{
                        position: 'fixed',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        background: 'rgba(0, 0, 0, 0.9)',
                        zIndex: 1000,
                    }}>
                        <EstadisticasModal setModalVisible={setEstadisticasView} modalVisible={estadisticasView} />
                    </div>
                }

                <div style={{ backgroundColor: 'black', position: 'fixed', bottom: 0, width: '100%', height: 41 }}>
                    <center style={{ color: 'white', marginTop: 5 }}>© MIDPIE</center>
                </div>

            </div>
        </>
    );

}
