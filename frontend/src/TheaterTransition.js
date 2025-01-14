// src/TheaterTransition.js
import React, { useEffect, useRef, useState } from 'react';
import './TheaterTransition.css';
// Import the sound file (adjust the relative path if needed)
import openSound from './static/audios/maderaAbriendose.mp3';

export default function TheaterTransition({ children, openWhen }) {
  // Whether curtains are fully open or not
  const [curtainsOpen, setCurtainsOpen] = useState(false);
  // Create a ref to store the audio object so that it's not recreated on every render
  const audioRef = useRef(null);

  useEffect(() => {
    if (!audioRef.current) {
      audioRef.current = new Audio(openSound);
    }
  }, []);

  useEffect(() => {
    if (openWhen) {
      // After a short delay, open the curtains and play the sound
      const timer = setTimeout(() => {
        setCurtainsOpen(true);
        // Reset the audio (in case it was played before) and play
        if (audioRef.current) {
          audioRef.current.currentTime = 0;
          audioRef.current.play();
        }
      }, 300); // adjust delay as needed
      return () => clearTimeout(timer);
    } else {
      // If openWhen is false, keep the curtains closed
      setCurtainsOpen(false);
    }
  }, [openWhen]);

  return (
    <div className="theater-container">
      {/* The content we want to show (the loading screen, login, or child route) */}
      {children}

      {/* Our curtain overlay on top; it opens if curtainsOpen === true */}
      <div className={`curtains ${curtainsOpen ? 'open' : ''}`}>
        <div className="curtain curtain-left" />
        <div className="curtain curtain-right" />
      </div>
    </div>
  );
}
