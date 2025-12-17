const API_BASE_URL = 'http://localhost:8080/api';

export interface ApiResponse<T = any> {
  data?: T;
  error?: string;
}

async function apiRequest<T = any>(
  endpoint: string,
  options: RequestInit = {}
): Promise<ApiResponse<T>> {
  const url = `${API_BASE_URL}${endpoint}`;
  console.log('🚀 API Request:', url, options.method || 'GET');
  console.log('📦 Request Body:', options.body);
  
  try {
    const response = await fetch(url, {
      ...options,
      credentials: 'include', // Include cookies for session-based authentication
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    console.log('📡 Response Status:', response.status, response.statusText);

    const data = await response.text();
    console.log('📥 Response Data (raw):', data);
    
    let parsedData: any;
    
    try {
      parsedData = data ? JSON.parse(data) : null;
      console.log('✅ Parsed Data:', parsedData);
    } catch {
      parsedData = data;
      console.log('⚠️ Could not parse as JSON, using raw data');
    }

    if (!response.ok) {
      const errorMsg = typeof parsedData === 'string' ? parsedData : parsedData?.message || 'Request failed';
      console.error('❌ API Error:', errorMsg);
      return {
        error: errorMsg,
      };
    }

    console.log('✅ API Success');
    return { data: parsedData };
  } catch (error) {
    console.error('💥 Network Error:', error);
    return {
      error: error instanceof Error ? error.message : 'Network error occurred',
    };
  }
}

// Auth APIs
export const authApi = {
  register: async (data: { name: string; email: string; phNumber: string; password: string }) => {
    return apiRequest<string>('/auth/register', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  login: async (data: { email: string; password: string }) => {
    return apiRequest<string>('/auth/login', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  verifyLoginOtp: async (data: { email: string; otp: string }) => {
    return apiRequest<string>('/auth/verify-login-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  resendLoginOtp: async (data: { email: string }) => {
    return apiRequest<string>('/auth/resend-login-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  verifyEmailOtp: async (data: { email: string; otp: string }) => {
    return apiRequest<string>('/auth/verify-email-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  verifyMobileOtp: async (data: { phNumber: string; otp: string }) => {
    return apiRequest<string>('/auth/verify-mobile-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  resendEmailOtp: async (data: { email: string }) => {
    return apiRequest<string>('/auth/resend-email-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  resendMobileOtp: async (data: { phNumber: string }) => {
    return apiRequest<string>('/auth/resend-mobile-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  forgotPassword: async (data: { email: string }) => {
    return apiRequest<string>('/auth/forgot-password', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  resetPassword: async (data: { email: string; otp: string; newPassword: string }) => {
    return apiRequest<string>('/auth/reset-password', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  resendForgotPasswordOtp: async (data: { email: string }) => {
    return apiRequest<string>('/auth/resend-forgot-password-otp', {
      method: 'POST',
      body: JSON.stringify(data),
    });
  },

  logout: async () => {
    return apiRequest<string>('/auth/logout', {
      method: 'POST',
    });
  },
};

// User APIs
export interface UserResponse {
  id: string;
  name: string;
  address?: string;
  age: number;
  email: string;
  phNumber: string;
  profileUrl?: string;
  role: string;
}

export interface UserRequest {
  name?: string;
  address?: string;
  age?: number;
  email?: string;
  phNumber?: string;
  profileUrl?: string;
  password?: string;
  role?: string;
}

export const userApi = {
  getCurrentUser: async () => {
    return apiRequest<UserResponse>('/users/me', {
      method: 'GET',
    });
  },

  getUser: async (id: string) => {
    return apiRequest<UserResponse[]>(`/users/get-user?id=${id}`, {
      method: 'GET',
    });
  },

  updateUser: async (id: string, data: UserRequest) => {
    return apiRequest<UserResponse>(`/users/${id}`, {
      method: 'PUT',
      body: JSON.stringify(data),
    });
  },

  getUserGroups: async (id: string) => {
    return apiRequest<any[]>(`/users/get-user-groups?id=${id}`, {
      method: 'GET',
    });
  },

  getUserBalanceSummary: async (id: string) => {
    return apiRequest<UserResponse>(`/users/get-user-balance-summary?id=${id}`, {
      method: 'GET',
    });
  },
};

