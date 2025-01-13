// src/TheaterTransition.js
import React, { useEffect, useState } from 'react';
import './TheaterTransition.css';

export default function TheaterTransition({ children, openWhen }) {
  // Whether curtains are fully open or not
  const [curtainsOpen, setCurtainsOpen] = useState(false);

  useEffect(() => {
    if (openWhen) {
      // After a short delay, open the curtains
      const timer = setTimeout(() => {
        setCurtainsOpen(true);
      }, 300); // adjust delay as needed
      return () => clearTimeout(timer);
    } else {
      // If openWhen is false, keep them closed
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
