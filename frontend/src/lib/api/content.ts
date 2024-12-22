const API_BASE = '/api/contents';

export const uploadVideo = async (file: File) => {
  const formData = new FormData();
  formData.append('video', file);

  const response = await fetch(`${API_BASE}/videos`, {
    method: 'POST',
    body: formData
  });
  return response.json();
};

export const uploadThumbnail = async (file: File) => {
  const formData = new FormData();
  formData.append('thumbnail', file);

  const response = await fetch(`${API_BASE}/thumbnails`, {
    method: 'POST',
    body: formData
  });
  return response.json();
}

export const uploadBlockImage = async (file: File) => {
  const formData = new FormData();
  formData.append('blockImage', file);

  const response = await fetch(`${API_BASE}/block-images`, {
    method: 'POST',
    body: formData
  });
  return response.json();
}