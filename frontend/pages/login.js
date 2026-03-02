import { useState } from 'react';
import axios from 'axios';
import { useRouter } from 'next/router';

export default function Login() {
  const [user, setUser] = useState('');
  const [pass, setPass] = useState('');
  const [status, setStatus] = useState(null);
  const [showOtp, setShowOtp] = useState(false);
  const [otpValue, setOtpValue] = useState('');
  const router = useRouter();

  const doLogin = async () => {
    try{
      const resp = await axios.post('/api/fraud', {
        scenario: 'login',
        userId: user || 'demo-user',
        deviceId: 'device-123',
        simSwap: false,
        location: 'Home',
      });
      const action = resp.data.action;
      setStatus(action);
      if(action === 'ok'){
        localStorage.setItem('isLoggedIn', 'true');
        setTimeout(()=> router.push('/dashboard'), 600);
      } else if(action === 'otp'){
        setShowOtp(true);
      }
    }catch(e){
      setStatus('block');
    }
  };

  const verifyOtp = () => {
    setShowOtp(false);
    setStatus('ok');
    localStorage.setItem('isLoggedIn', 'true');
    setTimeout(()=> router.push('/dashboard'), 500);
  };

  return (
    <div className="app-container">
      <div className="card login">
        <img src="/logo.png" className="logo" style={{width:100,  borderRadius:20}} alt="logo" />
        <h1>DemoBank</h1>
        <p className="muted">Accede a tu cuenta de demo</p>

        <input className="login-input" placeholder="Usuario (email)" value={user} onChange={e => setUser(e.target.value)} />
        <input className="login-input" placeholder="Password" type="password" value={pass} onChange={e => setPass(e.target.value)} />

        <button className="login-button" onClick={doLogin}>Login</button>

        {status === 'ok' && <div className="status ok">Acceso permitido</div>}
        {status === 'otp' && <div className="status otp">Verificación extra requerida</div>}
        {status === 'block' && <div className="status block">Acceso bloqueado por riesgo</div>}

        {showOtp && (
          <div className="login-otp" style={{marginTop:12}}>
            <input placeholder="Introduce OTP" value={otpValue} onChange={e=>setOtpValue(e.target.value)} />
            <button onClick={verifyOtp} style={{marginTop:8}}>Verificar OTP</button>
          </div>
        )}
      </div>
    </div>
  );
}
