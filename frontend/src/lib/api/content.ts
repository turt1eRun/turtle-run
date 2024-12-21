const API_BASE = '/api/contents';

export const uploadVideo = async (file: File) => {
  const formData = new FormData();
  formData.append('video', file);

  const response = await fetch(`${API_BASE}/videos`, {
    method: 'POST',
    body: formData,
  });
  return response.json();
};