import { useRouter } from 'next/router';

export default function Dashboard() {
  const router = useRouter();

  return (
    <div className="app-container">
      <div className="card">
        <img src="/logo-large.png" className="logo-large" alt="logo" />
        <h1>Dashboard</h1>
        <p className="muted">Saldo: 5.320€</p>
        <ul>
          <li><button onClick={() => router.push('/transfer')}>Transferir</button></li>
          <li><button onClick={() => router.push('/change-password')}>Cambiar contraseña</button></li>
          <li><button onClick={() => router.push('/pay')}>Pagar online</button></li>
        </ul>
      </div>
    </div>
  );
}
