import { Form } from "reactstrap";
import React, { useState, forwardRef } from 'react';
import '../components/css/joinModal.css';
import { IoCloseCircle } from "react-icons/io5";
import GameList from "./GameList";
import { IoIosSearch } from "react-icons/io";
import PartidaView from "./PartidaView";
import { VscChromeClose } from "react-icons/vsc";
import { IoIosArrowBack } from "react-icons/io";
import { IoIosArrowForward } from "react-icons/io";
import tokenService from "../services/token.service";

const jwt = tokenService.getLocalAccessToken();
const GetJoinModal = forwardRef((props, ref) => {

    const [message, setMessage] = useState(null);
    const [codigo, setCodigo] = useState("");
    const [partida, setPartida] = useState(null);

    const [pagina, setPagina] = useState(0);
    const [totalPaginas, setTotalPaginas] = useState(1);

    function handleModalVisible(setModalVisible, modalVisible) {
        setModalVisible(!modalVisible);
    }

    function handleSubmit(event) {
        setPartida(null)

        event.preventDefault();
        fetch(
            "/api/v1/partida/search?codigo=" + codigo,
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
                if (data && codigo && JSON.parse(data).estado !== 'FINISHED') {
                    setPartida(data)
                }
                else {
                    alert('No existe la partida de código ' + codigo)

                }
            })
            .catch((message) => alert(message + codigo));
    }

    function handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        setCodigo(value)
    }

    function handleReset() {
        setCodigo('');
        setPartida(null);
        document.getElementById('inputId').value = null;
    }

    function handleOtherPage(number) {
        setPagina(pagina + number);
    }


    return (
        <>
            <div className='cuadro-union' style={{ display: 'flex', flexDirection: 'column' }}>
                <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: 'absolute' }} onClick={() => handleModalVisible(props.setModalVisible, props.modalVisible)} />

                <Form onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                    <div style={{ display: 'flex', alignItems: 'center' }}>
                        <h1 style={{ fontSize: 24, whiteSpace: "nowrap" }}>
                            Buscar Partida:
                        </h1>
                        <input onChange={handleChange} style={{ color: "black" }} placeholder="código de partida" class="input" name="text" id='inputId' pattern="[A-Z0-9]{0,5}" type="text" required />


                        <div style={{ display: 'flex', alignItems: 'center', position: 'relative' }}>
                            <button type="submit" style={{ background: 'transparent', border: 'none', cursor: 'pointer' }}>
                                <IoIosSearch style={{ width: 30, height: 30 }} />
                            </button>
                            {partida &&
                                <VscChromeClose onClick={handleReset} style={{ width: 30, height: 30, cursor: "pointer", marginLeft: 8 }} />
                            }
                        </div>

                    </div>
                </Form>
                {partida &&
                    <PartidaView
                        game={JSON.parse(partida)}
                    />
                }
                <h1 style={{ fontSize: 24 }}>
                    Partidas públicas:
                </h1>
                <div style={{ overflowY: 'auto' }}>
                    <GameList
                        key={pagina}
                        pagina={pagina}
                        setTotalPaginas={setTotalPaginas}
                    />
                </div>

                <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: '10px', marginTop: 'auto', paddingTop: '10px' }}>

                    <IoIosArrowBack
                        style={{
                            fontSize: '24px',
                            cursor: pagina > 0 - 1 ? 'pointer' : 'default',
                            visibility: pagina > 0 ? 'visible' : 'hidden'
                        }}
                        onClick={() => pagina > 0 ? handleOtherPage(-1) : null} />

                    <p style={{ margin: 0, fontSize: '16px' }}>{pagina + 1}</p>
                    
                    <IoIosArrowForward
                        style={{
                            fontSize: '24px',
                            cursor: pagina < totalPaginas - 1 ? 'pointer' : 'default',
                            visibility: pagina < totalPaginas - 1 ? 'visible' : 'hidden'
                        }}
                        onClick={() => pagina < totalPaginas - 1 ? handleOtherPage(1) : null} />

                </div>
            </div>

        </>

    )
});
export default GetJoinModal;