import { useRouter } from 'next/router';

export default function Dashboard() {
  const router = useRouter();

  return (
    <div className="app-container">
      <div className="card">
        <img
          src="/logo.png"
          className="logo-large"
          alt="logo"
          style={{width:100, borderRadius:20, cursor:'pointer'}}
          onClick={() => router.push('/dashboard')}
        />
        <h1>Dashboard</h1>
        <p className="muted">Fraud Mitigation</p>
        <div className="dashboard-actions">
          <button className="dashboard-action-btn" onClick={() => router.push('/transfer')}>Transferir</button>
          <button className="dashboard-action-btn" onClick={() => router.push('/change-password')}>Cambiar contraseña</button>
          <button className="dashboard-action-btn" onClick={() => router.push('/pay')}>Pagar online</button>
        </div>
      </div>
    </div>
  );
}
