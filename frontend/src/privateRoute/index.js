// src/privateRoute.js

import React, { useState, useEffect } from 'react';
import tokenService from '../services/token.service';
import Login from '../auth/login';

// We'll import the TheaterTransition component
import TheaterTransition from '../TheaterTransition';

const PrivateRoute = ({ children }) => {
  const [isLoading, setIsLoading] = useState(true);
  const [isValid, setIsValid] = useState(false);
  const [message, setMessage] = useState(null);

  const jwt = tokenService.getLocalAccessToken();

  // Validate token on mount
  useEffect(() => {
    if (!jwt) {
      // No token => not valid => show Login
      setIsValid(false);
      setIsLoading(false);
    } else {
      // Validate by calling your backend
      fetch(`https://trucobeasts-e0dxg3dvccd5dvb5.centralus-01.azurewebsites.net/api/v1/auth/validate?token=${jwt}`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
          'Content-Type': 'application/json',
        },
      })
        .then((response) => response.json())
        .then((result) => {
          setIsValid(result);
          setIsLoading(false);

          if (!result) {
            setMessage('Your token has expired. Please, sign in again.');
          }
        })
        .catch(() => {
          setIsValid(false);
          setIsLoading(false);
          setMessage('Error verifying token. Please, sign in again.');
        });
    }
  }, [jwt]);

  // Decide what to render behind the curtains
  let content;
  if (isLoading) {
    // Still loading => show a "Loading..." screen
    content = (
      <div
        style={{
          color: 'white',
          fontSize: '40px',
          marginTop: '40vh',
          textAlign: 'center',
        }}
      >
        Loading...
      </div>
    );
  } else if (isValid) {
    // Token valid => show protected content
    content = children;
  } else {
    // Token invalid => show Login + message
    content = <Login message={message} navigation={true} />;
  }

  // The curtains open only after loading is done
  const openWhen = !isLoading;

  return (
    <TheaterTransition openWhen={openWhen}>
      {content}
    </TheaterTransition>
  );
};

export default PrivateRoute;
