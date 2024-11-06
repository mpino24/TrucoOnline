import { Button, Label, Form, Input } from "reactstrap";
import React, { useState, useCallback, forwardRef } from 'react';
import { Link, Router } from 'react-router-dom';
import 'frontend/src/components/css/joinModal.css';
import { IoCloseCircle } from "react-icons/io5";
import GameList from "./GameList";
import { ScrollView } from "react-native";
import { IoIosSearch } from "react-icons/io";
import PartidaView from "./PartidaView";
import { ImageBackground } from "react-native-web";

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
                if (data && codigo) {
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


    return (
        <>
            <div className='cuadro-union'>
                <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position:'absolute' }} onClick={() => handleModalVisible(props.setModalVisible, props.modalVisible)} />
                
                <Form onSubmit={handleSubmit} style={{ display: 'flex', alignItems: 'center' }}>
                    <div style={{ display:'flex', alignItems: 'center' }}>
                        <h1 style={{ fontSize: 24 , whiteSpace: "nowrap"}}>
                            Buscar Partida:
                        </h1>
                        <input onChange={handleChange} style={{ color: "black" }} placeholder="código de partida" class="input" name="text" pattern="[A-Z]{0,5}"  type="text" required />
                        <button style={{background: 'transparent', border:'transparent'}}>
                            <IoIosSearch style={{ width: 40, height: 40, cursor: 'pointer' }} />
                        </button>
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
                <div style={{overflowY:'auto'}}>
                <GameList />
                </div>
            </div>

        </>

    )
});
export default GetJoinModal;