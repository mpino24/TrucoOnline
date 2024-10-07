import { Button, Label, Form, Input } from "reactstrap";
import React, { useState, useCallback } from 'react';
import { Link, Router } from 'react-router-dom';
import 'frontend/src/components/css/joinModal.css';
import { IoCloseCircle } from "react-icons/io5";
import GameList from "./GameList";

export default function getJoinModal(setModalVisible, modalVisible = false) {
    
    function handleModalVisible(setModalVisible, modalVisible) {
        setModalVisible(!modalVisible);
    }

    return (
        <>
            <div className='cuadro-union'>
                <IoCloseCircle style={{ width: 30, height: 30, cursor: "pointer", position: "absolute" }} onClick={() => handleModalVisible(setModalVisible, modalVisible)} />
                <div style={{ display: 'flex', alignItems: 'center' }}>
                    <h1 style={{ fontSize: 24 }}>
                        Partida privada:
                    </h1>
                    <input placeholder="código de partida" class="input" name="text" type="text"></input>
                    <button class="button">
                        Unirse
                    </button>
                    <button class="button" style={{ color: 'blue' }}>
                        Ver
                    </button>
                </div>
                <h1 style={{ fontSize: 24 }}>
                    Partidas públicas:
                    <GameList/>
                </h1>


            </div>

        </>

    )
}