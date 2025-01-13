import React, { useState, useCallback, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { NavLink, NavItem, Nav } from 'reactstrap';
import { CSSTransition } from 'react-transition-group'; // <-- NEW IMPORT

import '../App.css';
import '../static/css/home/home.css';
import '../static/css/home/homeButton.css';

import jwt_decode from 'jwt-decode';

import { RiArrowRightDoubleLine, RiArrowLeftDoubleLine } from 'react-icons/ri';
import { FaUserFriends } from 'react-icons/fa';
import shuiDef from '../static/audios/shuiDef.mp3';
import shiuDef from '../static/audios/shiuDef.mp3';
import trofeo from '../static/images/Trofeo.png';
import amistad from '../static/images/amigos.png';

import tokenService from '../services/token.service.js';
import CreationModal from '../components/getCreationModal.js';
import GetJoinModal from '../components/getJoinModal.js';
import GetFriendsModal from '../components/getFriendsModal';
import EstadisticasModal from '../estadisticas/EstadisticasModal';

import useFetchState from '../util/useFetchState.js';

export default function Home() {
    const [joinModalView, setJoinModalView] = useState(false);
    const [creationModalView, setCreationModalView] = useState(false);
    const [friendsView, setFriendsView] = useState(false);
    const friendViewRef = useRef(friendsView);

    const [showAccountMenu, setShowAccountMenu] = useState(false);
    const [backgroundUrl, setBackgroundUrl] = useState("/fondos/fondo0.jpg");
    const [username, setUsername] = useState('');
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

    // Fetch player data
    const [player, setPlayer] = useFetchState(
        [],
        '/api/v1/jugador?userId=' + usuario.id,
        jwt,
        setMessage,
        setVisible
    );
    const playEntrySound = () => {
        const audio = new Audio(shuiDef); // path to your audio file
        audio.play().catch(err => console.error("Audio play error:", err));
      };
      const playExitSound = () => {
        const audio = new Audio(shiuDef); // path to your audio file
        audio.play().catch(err => console.error("Audio play error:", err));
      };
    // Fetch roles on mount
    useEffect(() => {
        if (jwt) {
            setRoles(jwt_decode(jwt).authorities);
        }
    }, [jwt]);

    // Toggles
    const toggleAccountMenu = useCallback(() => {
        setShowAccountMenu((current) => !current);
    }, []);

    const toggleJoinModal = useCallback(() => {
        const audio = new Audio('/duck.mp3');
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
    }, []);

    const toggleEstadisticasModal = useCallback(() => {
        setEstadisticasView((current) => !current);
    }, []);

    // Fetch the current game on mount
    function fetchCurrentGame() {
        fetch('/api/v1/partidajugador/partidaJugadorActual', {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
        })
            .then((response) => {
                if (response.status === 202) {
                    return null;
                }
                return response.json();
            })
            .then((data) => {
                if (data !== null) {
                    if (data.game && data.game.estado !== 'FINALIZADA') {
                        navigate('/partidas?partidaCode=' + data.game.codigo);
                    }
                }
            })
            .catch((message) => alert(message));
    }

    // Fetch unread messages
    function fetchNotReadMessages() {
        
        
        fetch('/api/v1/chat/unread', {
            method: 'GET',
            headers: {
                Authorization: `Bearer ${jwt}`,
            },
        })
            .then((response) => response.json())
            .then((data) => {
                if (data > 0 && numMessagesRef.current < data) {
                    // play a sound only if there are new messages
                    if (!friendViewRef.current) {
                        const audio = new Audio('/notification.mp3');
                        audio.play().catch((error) => {
                            console.error('Error al reproducir el sonido:', error);
                        });
                    }
                }
                setNumMessages(data);
            })
            .catch((message) => alert(message));
        
    }

    // Keep track of numMessages in a ref
    useEffect(() => {
        numMessagesRef.current = numMessages;
    }, [numMessages]);

    // Keep track of friendsView in a ref
    useEffect(() => {
        friendViewRef.current = friendsView;
    }, [friendsView]);

    // On initial mount
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
        const randomIndex = Math.floor(Math.random() * backgrounds.length);
        setBackgroundUrl(backgrounds[randomIndex]);

        fetchCurrentGame();
        fetchNotReadMessages();

        
    }, []);

    //Este useEffect es para traer los mensajes solo si los modales de creacion y union a partida estan cerrados
    //sino sale un error cuando en el timing justo creas la partida y se ejecuta esta llamada
    useEffect(() => {
        
        // Poll for unread messages
        const timer = setInterval(() => {
            if(!creationModalView && !joinModalView){
            fetchNotReadMessages();
            console.log("Soy yo muajaja")
            }
        }, 2000);

        return () => clearInterval(timer);
    }, [creationModalView, joinModalView]);

    // Re-fetch messages if we close the friendsView
    useEffect(() => {
        if(!creationModalView && !joinModalView){
            fetchNotReadMessages();
        }
    }, [friendsView, numMessages, creationModalView, joinModalView]);

    // If user is available, set the username and photo
    useEffect(() => {
        if (usuario) {
            setUsername(usuario.username);
            setPhotoUrl(player.photo);
        }
    }, [usuario, player]);

    const handleRedirect = (path) => {
        navigate(path);
    };

    return (
        <>

            {/* If NOT showing estadisticasView, show admin button if user is ADMIN */}
            {!estadisticasView && (
                <div>
                    {roles.includes('ADMIN') && (
                        <button
                            className="button-admin"
                            style={{marginLeft:"15%", position:"absolute"}}
                            onClick={() => {
                                navigate('/admin');
                            }}
                        >
                            ADMINISTRACIÓN
                        </button>
                    )}
                </div>
            )}

            {/* Account menu (only show if not in estadisticasView or friendsView) */}
            {!estadisticasView && !friendsView && (
                <Nav
                    className="ms-auto mb-2 mb-lg-0"
                    navbar
                    style={{ float: 'right', marginTop: 15, marginRight: 15 }}
                >
                    <div
                        style={{ display: 'flex', alignItems: 'center', cursor: 'pointer' }}
                        onClick={toggleAccountMenu}
                    >
                        <p className="loginText" style={{ marginRight: 20, fontSize: 25 }}>
                            {username}
                        </p>
                        <img
                            style={{ height: 60, width: 60, borderRadius: 500 }}
                            src={photoUrl}
                            alt="Foto de perfil del usuario"
                        />
                    </div>

                    <CSSTransition
                            in={showAccountMenu}
                            timeout={300}
                            classNames="join-modal"
                            unmountOnExit
                        >
                        <div
                        className='cuadro-creacion'
                            style={{
                                marginTop:"10px",
                                marginRight:"10px",
                                overflow:'hidden'
                            }}
                        >

                            
                            <NavItem className="d-flex">
                                <NavLink
                                    style={{ color: 'rgb(255, 223, 65)'}}
                                    id="perfil"
                                    tag={Link}
                                    to="/profile"
                                >
                                    Mi Perfil
                                </NavLink>
                            </NavItem>

                            <NavItem className="d-flex">
                                <NavLink
                                    style={{ color: 'rgb(255, 131, 112)'}}
                                    id="logout"
                                    tag={Link}
                                    to="/logout"
                                >
                                    Cerrar Sesión
                                </NavLink>
                            </NavItem>
                        </div>
                        </CSSTransition>
                    
                </Nav>
            )}

            <div
                className="home-page-container"
                style={{
                    backgroundImage: `url(${backgroundUrl})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    backgroundRepeat: 'no-repeat',
                    height: '100vh',
                    width: '100vw',
                }}
            >
                {!estadisticasView && (
                    <>
                        {/* Left trofeo button -> toggles Estadisticas */}
                        <div style={{ width: '36%' }}>
                            <div
                                style={{
                                    position: 'absolute',
                                    left: 0,
                                    top: '50%',
                                    transform: 'translateY(-50%)',
                                    cursor: 'pointer',
                                }}
                                onClick={toggleEstadisticasModal}
                            >
                                <div
                                    className="sideflip-container"
                                    style={{ width: 80, height: 80, float: 'left' }}
                                >
                                    <img
                                        className="ImageStyle2"
                                        style={{ width: 80, height: 80, float: 'left' }}
                                        src={trofeo}
                                        alt="Trofeo"
                                    />
                                </div>
                                <RiArrowRightDoubleLine
                                    style={{
                                        width: 80,
                                        height: 80,
                                        float: 'left',
                                        color: 'gold',
                                    }}
                                />
                                <div />
                            </div>
                        </div>

                        {/* JOIN MODAL (wrapped in CSSTransition) */}
                        <CSSTransition
                            in={joinModalView}
                            timeout={300}
                            classNames="join-modal"
                            unmountOnExit
                        >
                            <div
                                style={{
                                    position: 'fixed',
                                    top: '50%',
                                    left: '50%',
                                    transform: 'translate(-50%, -50%)',
                                    zIndex: 1000,
                                    width: '60%',
                                }}
                            >
                                <GetJoinModal
                                    setModalVisible={setJoinModalView}
                                    modalVisible={joinModalView}
                                />
                            </div>
                        </CSSTransition>

                        {/* CREATION MODAL (wrapped in CSSTransition) */}
                        <CSSTransition
                            in={creationModalView}
                            timeout={300}
                            classNames="creation-modal"
                            unmountOnExit
                        >
                            <div
                                style={{
                                    position: 'fixed',
                                    top: '50%',
                                    left: '50%',
                                    transform: 'translate(-50%, -50%)',
                                    zIndex: 1000,
                                }}
                            >
                                <CreationModal
                                    setCreationModalView={setCreationModalView}
                                    creationModalView={creationModalView}
                                />
                            </div>
                        </CSSTransition>

                        {/* Only show center hero buttons if both modals are hidden */}
                        {!(joinModalView || creationModalView) && (
                            <div
                                className="hero-div"
                                style={{
                                    position: 'fixed',
                                    top: '50%',
                                    left: '50%',
                                    transform: 'translate(-50%, -50%)',
                                    textAlign: 'center',
                                }}
                            >
                                <h1 className="loginText" style={{ color: 'rgb(255, 223, 65)' }}>
                                    ¿Un truco?
                                </h1>

                                <button className="auth-button" onClick={toggleCreationModal}>
                                    Crear
                                </button>
                                <button
                                    className="auth-button"
                                    style={{ marginTop: '10px' }}
                                    onClick={toggleJoinModal}
                                >
                                    Unirte
                                </button>
                            </div>
                        )}

                        {/* Right amistad button -> toggles Friends */}
                        {!friendsView && (
                            <div style={{ width: '36%' }}>
                                <div
                                    key={numMessages}
                                    style={{
                                        position: 'absolute',
                                        right: 0,
                                        top: '50%',
                                        transform: 'translateY(-50%)',
                                        cursor: 'pointer',
                                        overflow: 'hidden',
                                    }}
                                    onClick={toggleFriendsModal}
                                >
                                    <img
                                        className="ImageStyle3"
                                        style={{ width: 80, height: 80, float: 'right' }}
                                        src={amistad}
                                        alt="Amigos"
                                    />
                                    <RiArrowLeftDoubleLine
                                        style={{
                                            width: 80,
                                            height: 80,
                                            float: 'right',
                                            color: 'darkorange',
                                        }}
                                    />
                                    {numMessages > 0 && (
                                        <span
                                            style={{
                                                color: 'white',
                                                backgroundColor: 'orange',
                                                borderRadius: '50%',
                                                position: 'absolute',
                                                top: '50px',
                                                right: '0px',
                                                width: '30px',
                                                height: '30px',
                                                display: 'flex',
                                                alignItems: 'center',
                                                justifyContent: 'center',
                                                fontSize: '20px',
                                            }}
                                        >
                                            {numMessages}
                                        </span>
                                    )}
                                </div>
                            </div>
                        )}

                        {/* FRIENDS MODAL */}
                        <CSSTransition
                                in={friendsView}               // controls when it is shown
                                timeout={300}                  // duration in ms (adjust as needed)
                                classNames="slide-right"       // base name for our CSS classes
                                unmountOnExit   
                                onEntered={playEntrySound}
                                onExited={playExitSound}
                                             // unmount the modal once hidden
                                >
                            <div
                                style={{
                                    position: 'fixed',
                                    right: 0,
                                    top: 0,
                                    height: '100%',
                                    width: '36%',
                                }}
                            >
                                <GetFriendsModal
                                    setModalVisible={setFriendsView}
                                    modalVisible={friendsView}
                                />
                            </div>
                        </CSSTransition>
                    </>
                )}

                {/* ESTADISTICAS MODAL */}
                {estadisticasView && (
                    <div
                        style={{
                            position: 'fixed',
                            top: 0,
                            left: 0,
                            width: '100%',
                            height: '100%',
                            background: 'rgba(0, 0, 0, 0.9)',
                            zIndex: 1000,
                        }}
                    >
                        <EstadisticasModal
                            setModalVisible={setEstadisticasView}
                            modalVisible={estadisticasView}
                        />
                    </div>
                )}

                {/* Footer */}
                <div
                    style={{
                        backgroundColor: 'black',
                        position: 'fixed',
                        bottom: 0,
                        width: '100%',
                        height: 41,
                    }}
                >
                    <center style={{ color: 'white', marginTop: 5 }}>© MIDPIE</center>
                </div>
            </div>
        </>
    );
}
