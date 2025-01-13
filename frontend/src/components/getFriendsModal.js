import { forwardRef, useEffect, useState } from 'react';
import { Form } from "reactstrap";
import { IoIosSearch } from "react-icons/io";
import JugadorView from './JugadorView';
import JugadorList from "./JugadorList";
import { VscChromeClose } from "react-icons/vsc";
import { IoCloseCircle } from "react-icons/io5";
import tokenService from '../services/token.service.js';
import { FaRegEnvelope } from "react-icons/fa6";
import SolicitudList from './SolicitudList';
import { FaRegEnvelopeOpen } from "react-icons/fa6";
import { BsEnvelopePaper } from "react-icons/bs";
import Chat from "./showChat.js";
import shuiDef from '../static/audios/shuiDef.mp3';
import shiuDef from '../static/audios/shiuDef.mp3';



 const playEntrySound = () => {
        const audio = new Audio(shuiDef); // path to your audio file
        audio.play().catch(err => console.error("Audio play error:", err));
      };
      const playExitSound = () => {
        const audio = new Audio(shiuDef); // path to your audio file
        audio.play().catch(err => console.error("Audio play error:", err));
      };
const GetFriendsModal = forwardRef((props, ref) => {
    const [player, setPlayer] = useState(null);
    const [userName, setUsername] = useState("");
    const user = tokenService.getUser();
    const jwt = tokenService.getLocalAccessToken();

    const [message, setMessage] = useState(null);
    const [visible, setVisible] = useState(false);
    const [amigos, setAmigos] = useState([]);
    const [request, setRequest] = useState([]);
    const [requestView, setRequestView] = useState(false);
    const [chatVisible, setChatVisible] = useState(false);
    const [chatId, setChatId] = useState(null);
    const [jugadorChat, setJugadorChat] = useState(null);

    useEffect(() => {
        if (!userName) {
            fetchFriends();
            fetchFriendsRequest();

            const interval = setInterval(() => {
                fetchFriends();
                fetchFriendsRequest();
            }, 5000);
            return () => clearInterval(interval);

        }
    }, [jwt, userName])

    function fetchFriends() {
        fetch(
            `/api/v1/jugador/amigos?userId=` + user.id,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                }
            }
        )
            .then((response) => response.text())
            .then((data) => {
                setAmigos(JSON.parse(data))
                

            })
            .catch((message) => alert(message));

    }


    function handleSubmit(event) {
        setPlayer(null)

        event.preventDefault();
        fetch(
            "/api/v1/jugador/" + userName,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                }
            }
        )
            .then((response) => response.text())
            .then((data) => {

                if (data.includes('"statusCode":40')) {
                    alert('Jugador ' + userName + ' no encontrado')
                } else {
                    setPlayer(JSON.parse(data))
                    console.log(data)
                }
            })
            .catch((message) => alert(message + userName));
    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        setUsername(value)
    }

    function handleReset() {
        setPlayer(null);
        setUsername('');
        document.getElementById('inputId').value = '';
        fetchFriends();
        fetchFriendsRequest();
    }

    function handleModalVisible(setModalVisible, modalVisible) {
        setModalVisible(!modalVisible);
    }

    function fetchFriendsRequest() {
        fetch(
            `/api/v1/jugador/solicitudes?userId=` + user.id,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                    Accept: "application/json",
                    "Content-Type": "application/json",
                }
            }
        )
            .then((response) => response.text())
            .then((data) => {
                if (data.length === 0) {
                    setRequest([]);
                } else {
                    setRequest(JSON.parse(data))
                }
            })
            .catch((message) => alert(message));
    }

    function mostrarChat(player) {
        fetch(
            `/api/v1/chat/with/` + player.id,
            {
                method: "GET",
                headers: {
                    Authorization: `Bearer ${jwt}`,
                }
            }
        )
            .then((response) => response.json())
            .then((data) => {
                if (data.length === 0) {
                    setChatId(null);
                    setJugadorChat(null);

                } else {
                    setChatId(data.id);
                    setJugadorChat(player);

                }
            })
            .catch((message) => alert(message));
        setChatVisible(true);
    }

    function closeModal() {
        handleModalVisible(props.setModalVisible, props.modalVisible)

    }


    return (
        <div
            style={{
                display: 'flex',
                flexDirection: 'column',
                justifyContent: 'space-between',
                alignItems: 'stretch',
                height: '100vh',
            }}
        >
            <div style={{ backgroundImage: 'url(/fondos/fondoAmigosModal.png)', backgroundSize: 'cover', backgroundRepeat: 'no-repeat', backgroundPosition: 'center', height: '100%', width: '100%', overflow: 'hidden', paddingBottom: '60px' }}>

                {!chatVisible &&
                    <>

                        <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute',top:10,left:10, zIndex:1000 ,color: "rgb(123, 27, 0)"}} onClick={() => closeModal()} />
                        <h1 className='loginText' style={{ fontSize: 30, textAlign: "center", display: "flex", justifyContent: "center", alignItems: "center", gap: "10px",marginTop: "10px",position: "relative" }}>Amigos</h1>

                    
                        <hr></hr>
                        <div style={{ display: 'flex', alignItems: 'center' }}>
                            <Form onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                                <div style={{ display: 'flex', alignItems: 'center' }}>
                                    <input onChange={handleChange} style={{ color: "black" }} id='inputId' placeholder="Buscar..." class="input" name="text" type="text" required />
                                    <div>
                                        <button style={{ background: 'transparent', border: 'transparent' }}>
                                            <IoIosSearch style={{ width: 40, height: 40, cursor: 'pointer' }} />

                                        </button>
                                        {player &&
                                            <VscChromeClose style={{ width: 40, height: 40, cursor: "pointer", position: 'absolute', marginLeft: 20 }} onClick={() => handleReset()} />
                                        }

                                    </div>
                                </div>
                            </Form>
                            {!requestView && request.length === 0 &&
                                <button onClick={() => { setRequestView(true) }} style={{ background: 'transparent', border: 'transparent' }}>
                                    <FaRegEnvelope style={{ width: 40, height: 40, cursor: 'pointer', marginLeft: 60 }} />
                                </button>
                            }
                            {!requestView && request && request.length !== 0 &&
                                <button onClick={() => { setRequestView(true) }} style={{ background: 'transparent', border: 'transparent' }}>
                                    <BsEnvelopePaper style={{ width: 40, height: 40, cursor: 'pointer', marginLeft: 60 }} />
                                </button>
                            }
                            {requestView &&
                                <button onClick={() => { setRequestView(false) }} style={{ background: 'transparent', border: 'transparent' }}>
                                    <FaRegEnvelopeOpen style={{ width: 40, height: 40, cursor: 'pointer', marginLeft: 60 }} />
                                </button>

                            }
                        </div>
                        {player &&
                            <JugadorView jugador={player} />}
                        {!player && !requestView &&
                            <div style={{ overflowY: 'auto', maxHeight: 'calc(100vh - 170px)' }}>
                                <JugadorList jugadores={amigos}
                                    mostrarChat={mostrarChat} />
                            </div>
                        }
                        {requestView &&
                            <div style={{ overflowY: 'auto', maxHeight: 'calc(100vh - 160px)' }}>
                                <SolicitudList jugadores={request}
                                    setJugadores={setRequest} />
                            </div>
                        }
                    </>
                }
                {chatVisible &&
                    <>
                        {chatId &&
                            <Chat
                                idChat={chatId}
                                player={jugadorChat}
                                setChatVisible={setChatVisible}
                            />
                        }
                    </>
                }
            </div>
        </div>
    )
}
)
export default GetFriendsModal;
