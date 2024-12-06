import { Form } from "reactstrap";
import React, { useState, forwardRef } from 'react';
import '../components/css/joinModal.css';
import { IoCloseCircle } from "react-icons/io5";
import GameList from "./GameList";
import { IoIosSearch } from "react-icons/io";
import PartidaView from "./PartidaView";
import { VscChromeClose } from "react-icons/vsc";

const GetJoinModal = forwardRef((props, ref) => {

    const [message, setMessage] = useState(null);
    const [codigo, setCodigo] = useState("");
    const [partida, setPartida] = useState(null);

    function handleModalVisible(setModalVisible, modalVisible) {
        setModalVisible(!modalVisible);
    }

    function handleSubmit(event) {
        setPartida(null)

        event.preventDefault();
        fetch(
            "/api/v1/partida/search?codigo=" + codigo,
            {
                method: "GET"
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


    return (
        <>
            <div className='cuadro-union'>
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
                                <VscChromeClose onClick={handleReset} style={{width: 30,height: 30,cursor: "pointer",marginLeft: 8}}/>
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
                    <GameList />
                </div>
            </div>

        </>

    )
});
export default GetJoinModal;