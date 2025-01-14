import React, { useState, useEffect, forwardRef } from 'react';
import { useNavigate } from "react-router-dom";
import "../static/css/mano/finishModal.css";
import useFetchState from "../util/useFetchState";
import tokenService from "../services/token.service.js";

// Import images from the static folder
import trophyImage from "../static/images/Trofeo.png";
import sadFaceImage from "../static/images/bananapeel.png";
import winnerGif from "../static/images/CocodriloVictorioso.gif";
import loserGif from "../static/images/macacoRuinoso.gif";

// Import audio files
import winnerMusic from "../static/audios/ganasteBuenardo.mp3";
import loserMusic from "../static/audios/perdisteAlpiste.mp3";

const jwt = tokenService.getLocalAccessToken();

const FinishedModal = forwardRef((props, ref) => {
  const [message, setMessage] = useState(null);
  const [visible, setVisible] = useState(false);
  const navigate = useNavigate(); // Initialize the navigate function

  const [posicion, setPosicion] = useFetchState(
    {},
    `https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/partidajugador/miposicion/${props.game.id}`,
    jwt,
    setMessage,
    setVisible
  );

  // Determine winning team
  const ganador =
    props.game.puntosEquipo1 === props.game.puntosMaximos ? 0 : 1;
  const isWinner = posicion % 2 === ganador;

  // Decide which images to use
  const centerImage = isWinner ? trophyImage : sadFaceImage;
  const orbitGifPath = isWinner ? winnerGif : loserGif;

  // Animation duration (in seconds)
  const duration = 6;

  useEffect(() => {
    const audio = new Audio(isWinner ? winnerMusic : loserMusic);

    const handleVisibilityChange = () => {
      if (document.visibilityState === "visible") {
        // Play audio only if the page is visible
        audio.play().catch((error) => {
          console.error("Error playing audio:", error);
        });
      } else {
        // Pause audio if the page is not visible
        audio.pause();
      }
    };

    // Attach visibility change event listener
    document.addEventListener("visibilitychange", handleVisibilityChange);

    // Play audio initially if the page is visible
    if (document.visibilityState === "visible") {
      audio.play().catch((error) => {
        console.error("Error playing audio:", error);
      });
    }

    // Cleanup on unmount
    return () => {
      audio.pause();
      audio.currentTime = 0;
      document.removeEventListener("visibilitychange", handleVisibilityChange);
    };
  }, [isWinner]);

  const handleVolver = () => {
    navigate("/home");
  };

  return (
    <div className="finish-modal-container">
      <div
        className="finish-modal-background"
        style={{ backgroundImage: `url(/fondos/fondoPlayingModal.jpg)` }}
      >
        {/* Headings */}
        <div className="loginText" style={{ marginLeft: "70%", marginTop: "5%" }}>
          <h2 style={{ color: "rgb(255, 223, 65)" }}>Ganador: Equipo {ganador + 1}</h2>
          <h3 style={{ marginLeft: "5%", color: "rgb(255, 223, 65)" }}>
            {isWinner ? "Ganaste" : "Perdiste"}
          </h3>
        </div>

        {/* Spinner wrapper: centered container for orbiting GIFs and fixed center image */}
        <div className="spinner-wrapper">
          {[...Array(9)].map((_, index) => (
            <div
              key={index}
              className="orbit-item"
              style={{
                animationDelay: `-${(duration / 9) * index}s`
              }}
            >
              <img
                src={orbitGifPath}
                alt={`orbiting-gif-${index}`}
                className="orbiting-gif"
              />
            </div>
          ))}
          <img
            className="center-image"
            src={centerImage}
            alt={isWinner ? "Trophy" : "Sad face"}
          />
        </div>

        {/* Button to go home */}
        <div>
          <button
            className="auth-button"
            style={{ position: "absolute", top: "80%", left: "45%" }}
            onClick={handleVolver} // Use navigate on button click
          >
            Volver
          </button>
        </div>
      </div>
    </div>
  );
});

export default FinishedModal;
