const API_BASE_URL = 'http://localhost:8080/api';

export interface ApiResponse<T = any> {
  data?: T;
  error?: string;
}

async function apiRequest<T = any>(
  endpoint: string,
  options: RequestInit = {}
): Promise<ApiResponse<T>> {
  try {
    const response = await fetch(`${API_BASE_URL}${endpoint}`, {
      ...options,
      headers: {
        'Content-Type': 'application/json',
        ...options.headers,
      },
    });

    const data = await response.text();
    let parsedData: any;
    
    try {
      parsedData = data ? JSON.parse(data) : null;
    } catch {
      parsedData = data;
    }

    if (!response.ok) {
      return {
        error: typeof parsedData === 'string' ? parsedData : parsedData?.message || 'Request failed',
      };
    }

    return { data: parsedData };
  } catch (error) {
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
};

