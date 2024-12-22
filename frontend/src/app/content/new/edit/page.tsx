// content/new/edit/page.tsx
'use client';

import { FC, useRef, useState } from 'react';
import { useSearchParams } from 'next/navigation';
import { UploadCloud } from 'lucide-react';
import { uploadThumbnail, uploadBlockImage } from '@/lib/api/content';

interface BlockData {
  text?: string;
  descFileId?: number;
  orderNum: number;
}

interface ContentForm {
  title: string;
  videoFileId: number;
  thumbnailFileId: number | null;
  blockRequests: BlockData[];
}

interface UploadResponse {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

const EditPage: FC = () => {
  const searchParams = useSearchParams();
  const thumbnailInputRef = useRef<HTMLInputElement>(null);
  const [thumbnailPreviewUrl, setThumbnailPreviewUrl] = useState<string>('');
  const [thumbnailResponse, setThumbnailResponse] = useState<UploadResponse | null>(null);

  // 쿼리 파라미터에서 바로 데이터 추출
  const videoData: UploadResponse = {
    id: Number(searchParams.get('videoId')),
    uploadUrl: searchParams.get('uploadUrl') || '',
    originalFileName: searchParams.get('originalFileName') || ''
  };

  const [form, setForm] = useState<ContentForm>({
    title: '',
    videoFileId: videoData.id,
    thumbnailFileId: null,
    blockRequests: []
  });

  const handleThumbnailUpload = async (file: File) => {
    try {
      const response = await uploadThumbnail(file);
      setForm(prev => ({ ...prev, thumbnailFileId: response.id }));
      setThumbnailPreviewUrl(response.uploadUrl);
      setThumbnailResponse(response);
    } catch (error) {
      console.error('Thumbnail upload failed:', error);
    }
  };

  const addTextBlock = () => {
    setForm(prev => ({
      ...prev,
      blockRequests: [
        ...prev.blockRequests,
        {
          text: '',
          orderNum: prev.blockRequests.length + 1
        }
      ]
    }));
  };

  const addImageBlock = async (file: File) => {
    try {
      const response = await uploadBlockImage(file);
      setForm(prev => ({
        ...prev,
        blockRequests: [
          ...prev.blockRequests,
          {
            descFileId: response.id,
            orderNum: prev.blockRequests.length + 1
          }
        ]
      }));
    } catch (error) {
      console.error('Image upload failed:', error);
    }
  };

  const updateTextBlock = (index: number, content: string) => {
    const newBlocks = [...form.blockRequests];
    newBlocks[index] = {
      ...newBlocks[index],
      text: content
    };
    setForm(prev => ({ ...prev, blockRequests: newBlocks }));
  };

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
      // TODO: 에러 처리
      console.error('Failed to submit content:', error);
    }
  };

  return (
      <div className="w-full mx-auto p-8">
        <h1 className="text-2xl text-black font-bold mb-6">컨텐츠 업로드</h1>

        <div className="w-full p-8 items-center">
          {/* 스텝 네비게이션 */}
          <div className="w-full grid grid-cols-3 mb-6">
            <div className="flex items-center font-semibold text-lg">
              <span className="text-[#c5ccde] mr-2">1.</span>
              <span className="text-[#c5ccde]">동영상 업로드</span>
            </div>
            <div className="flex items-center justify-center font-semibold text-lg">
              <span className="text-[#4285f4] mr-2">2.</span>
              <span className="text-[#4285f4]">정보 입력</span>
            </div>
            <div></div>
          </div>

          {/* 메인 컨텐츠 영역 */}
          <div className="grid grid-cols-2 gap-6">
            {/* 왼쪽: 비디오 프리뷰 */}
            <div className="bg-white rounded-lg border border-gray-200 h-[400px] overflow-hidden">
              <video
                  src={videoData.uploadUrl}
                  controls
                  poster={thumbnailPreviewUrl}
                  className="w-full h-full object-contain"
              />
            </div>

            {/* 오른쪽: 제목 입력과 파일 업로드 */}
            <div className="flex flex-col gap-4 h-[400px]">
              <input
                  type="text"
                  value={form.title}
                  onChange={(e) => setForm(prev => ({...prev, title: e.target.value}))}
                  placeholder="제목을 입력해주세요"
                  className="w-full p-4 border border-gray-200 rounded-lg text-gray-600"
              />

              <div
                  className="bg-white border-2 border-dashed border-gray-200 rounded-lg p-6 flex-1 flex flex-col items-center justify-center"
                  onDrop={(e) => {
                    e.preventDefault();
                    const file = e.dataTransfer.files[0];
                    if (file) handleThumbnailUpload(file);
                  }}
                  onDragOver={(e) => e.preventDefault()}
              >
                {form.thumbnailFileId && thumbnailResponse ? (
                    <div className="flex flex-col items-center">
                      <p className="text-gray-700">{thumbnailResponse.originalFileName}</p>
                      <button
                          onClick={() => thumbnailInputRef.current?.click()}
                          className="mt-2 text-blue-500 hover:text-blue-600"
                      >
                        다시 업로드
                      </button>
                    </div>
                ) : (
                    <>
                      <UploadCloud className="w-12 h-12 text-gray-400 mb-2" />
                      <p className="text-gray-600 text-sm">썸네일 파일을 드래그하거나</p>
                      <button
                          onClick={() => thumbnailInputRef.current?.click()}
                          className="mt-2 text-blue-500 hover:text-blue-600"
                      >
                        파일 선택
                      </button>
                    </>
                )}
              </div>
            </div>
          </div>

          {/* 설명 작성 영역 */}
          <div className="mt-8 bg-white rounded-lg border border-gray-200 p-8">
         <textarea
             value={form.blockRequests[0]?.text || ''}
             onChange={(e) => updateTextBlock(0, e.target.value)}
             placeholder="컨텐츠에 대한 설명을 입력해주세요..."
             className="w-full min-h-[200px] p-4 border border-gray-200 rounded-lg resize-none"
         />

            {/* 이미지 추가 버튼 */}
            <div className="mt-4 flex justify-end">
              <button
                  onClick={() => document.getElementById('blockImageInput')?.click()}
                  className="px-4 py-2 bg-gray-100 text-gray-600 rounded-lg hover:bg-gray-200"
              >
                이미지 추가
              </button>
            </div>

            {/* 이미지 블록들 */}
            <div className="mt-4 space-y-4">
              {form.blockRequests.map((block, index) => (
                  block.descFileId && (
                      <div key={index} className="relative group">
                        <img
                            src={`/api/files/${block.descFileId}`}
                            alt="설명 이미지"
                            className="w-full rounded-lg"
                        />
                        <button
                            onClick={() => {
                              const newBlocks = [...form.blockRequests];
                              newBlocks.splice(index, 1);
                              setForm(prev => ({ ...prev, blockRequests: newBlocks }));
                            }}
                            className="absolute top-2 right-2 p-2 bg-black/50 text-white rounded-full opacity-0 group-hover:opacity-100 transition-opacity"
                        >
                          ✕
                        </button>
                      </div>
                  )
              ))}
            </div>
          </div>

          {/* Hidden inputs */}
          <input
              type="file"
              ref={thumbnailInputRef}
              className="hidden"
              accept="image/*"
              onChange={(e) => {
                const file = e.target.files?.[0];
                if (file) handleThumbnailUpload(file);
              }}
          />
          <input
              id="blockImageInput"
              type="file"
              className="hidden"
              accept="image/*"
              onChange={(e) => {
                const file = e.target.files?.[0];
                if (file) addImageBlock(file);
              }}
          />

          {/* 등록 버튼 */}
          <div className="flex justify-end mt-8">
            <button
                onClick={handleSubmit}
                className="px-4 py-2 bg-[#4285f4] text-white rounded-full font-semibold text-sm hover:bg-[#4285f4]/90"
            >
              작성완료
            </button>
          </div>
        </div>
      </div>
  );
};

export default EditPage;