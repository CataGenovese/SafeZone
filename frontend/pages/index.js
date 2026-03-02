import { useEffect } from 'react';
import { useRouter } from 'next/router';

export default function Home() {
  const router = useRouter();

  useEffect(() => {
    const isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
    router.replace(isLoggedIn ? '/dashboard' : '/login');
  }, [router]);

  return null;
}
