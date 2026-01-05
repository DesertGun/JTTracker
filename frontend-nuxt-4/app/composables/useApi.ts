export const useApi = () => {
  const config = useRuntimeConfig()
  const { session } = useUserSession()

  const apiFetch = async (url: string, options: any = {}) => {
    const fetchOptions: any = {
      baseURL: config.public.apiBaseUrl,
      headers: {
        ...options.headers,
        ...(session.value?.accessToken && {
          Authorization: `Bearer ${session.value.accessToken}`,
        }),
      },
      ...options,
    }

    // Handle arraybuffer responseType
    if (options.responseType === 'arraybuffer') {
      const response = await fetch(`${config.public.apiBaseUrl}${url}`, {
        ...fetchOptions,
        headers: fetchOptions.headers,
      })
      return await response.arrayBuffer()
    }

    return $fetch(url, fetchOptions)
  }

  return {
    get: (url: string, options?: any) => apiFetch(url, { ...options, method: 'GET' }),
    post: (url: string, body?: any, options?: any) => apiFetch(url, { ...options, method: 'POST', body }),
    put: (url: string, body?: any, options?: any) => apiFetch(url, { ...options, method: 'PUT', body }),
    delete: (url: string, options?: any) => apiFetch(url, { ...options, method: 'DELETE' }),
  }
}