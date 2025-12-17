import { createContext, useContext, useState, useEffect, ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';

interface AuthContextType {
  isAuthenticated: boolean;
  user: { email: string; name: string } | null;
  login: (email: string, name: string) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  // Initialize state from localStorage immediately
  const getInitialAuth = () => {
    try {
      const storedAuth = localStorage.getItem('auth');
      if (storedAuth) {
        const authData = JSON.parse(storedAuth);
        return {
          isAuthenticated: true,
          user: authData.user
        };
      }
    } catch (error) {
      console.error('Error reading auth from localStorage:', error);
      localStorage.removeItem('auth');
    }
    return {
      isAuthenticated: false,
      user: null
    };
  };

  const initialAuth = getInitialAuth();
  const [isAuthenticated, setIsAuthenticated] = useState(initialAuth.isAuthenticated);
  const [user, setUser] = useState<{ email: string; name: string } | null>(initialAuth.user);

  useEffect(() => {
    // Sync with localStorage on mount
    const storedAuth = localStorage.getItem('auth');
    if (storedAuth) {
      try {
        const authData = JSON.parse(storedAuth);
        setIsAuthenticated(true);
        setUser(authData.user);
      } catch {
        localStorage.removeItem('auth');
        setIsAuthenticated(false);
        setUser(null);
      }
    }
  }, []);

  const login = (email: string, name: string) => {
    console.log('🔐 AuthContext.login called with:', { email, name });
    setIsAuthenticated(true);
    setUser({ email, name });
    const authData = { user: { email, name } };
    localStorage.setItem('auth', JSON.stringify(authData));
    console.log('✅ Auth state updated:', { isAuthenticated: true, user: { email, name } });
  };

  const logout = () => {
    setIsAuthenticated(false);
    setUser(null);
    localStorage.removeItem('auth');
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, user, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};

