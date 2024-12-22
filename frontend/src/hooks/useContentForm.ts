// hooks/useContentForm.ts
import { useState } from 'react';
import { useSearchParams } from 'next/navigation';
import { Block, ContentForm, UploadResponse, VideoData } from '@/types/content';
import { uploadThumbnail, uploadBlockImage } from '@/lib/api/content';

export const useContentForm = () => {
  const searchParams = useSearchParams();

  // 비디오 데이터 추출
  const videoData: VideoData = {
    id: Number(searchParams.get('videoId')),
    uploadUrl: searchParams.get('uploadUrl') || '',
    originalFileName: searchParams.get('originalFileName') || ''
  };

  // 폼 상태
  const [form, setForm] = useState<ContentForm>({
    title: '',
    videoFileId: videoData.id,
    thumbnailFileId: null,
    blockRequests: []
  });

  // 썸네일 상태
  const [thumbnailFile, setThumbnailFile] = useState<UploadResponse | null>(null);

  // 제목 변경 핸들러
  const handleTitleChange = (title: string) => {
    setForm(prev => ({ ...prev, title }));
  };

  // 썸네일 업로드 핸들러
  const handleThumbnailUpload = async (file: File) => {
    try {
      const response = await uploadThumbnail(file);
      setForm(prev => ({ ...prev, thumbnailFileId: response.id }));
      setThumbnailFile(response);
    } catch (error) {
      console.error('Thumbnail upload failed:', error);
    }
  };

  // 블록 이미지 업로드 핸들러
  const handleImageUpload = async (file: File) => {
    try {
      const response = await uploadBlockImage(file);
      const newBlock: Block = {
        id: crypto.randomUUID(),
        type: 'image',
        content: response.uploadUrl,
        imageFile: response,
        orderNum: form.blockRequests.length + 1
      };

      setForm(prev => ({
        ...prev,
        blockRequests: [...prev.blockRequests, newBlock]
      }));
    } catch (error) {
      console.error('Image upload failed:', error);
    }
  };

  // 블록 업데이트 핸들러
  const updateBlocks = (blocks: Block[]) => {
    setForm(prev => ({ ...prev, blockRequests: blocks }));
  };

  // 폼 제출 핸들러
  const handleSubmit = async () => {
    if (!form.title || !form.thumbnailFileId || form.blockRequests.length === 0) {
      // TODO: 에러 처리
      return;
    }

    try {
      const response = await fetch('/api/contents', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(form)
      });

      if (response.ok) {
        // TODO: 성공 처리 (예: 목록 페이지로 이동)
      }
    } catch (error) {
      console.error('Failed to submit content:', error);
    }
  };

  return {
    form,
    videoData,
    thumbnailFile,
    handleTitleChange,
    handleThumbnailUpload,
    handleImageUpload,
    updateBlocks,
    handleSubmit
  };
};

export default useContentForm;