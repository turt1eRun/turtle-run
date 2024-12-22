// content/new/page.tsx
'use client';

import { useRouter } from 'next/navigation';
import { useState } from 'react';
import VideoUploader from "@/components/content/VideoUploader";

interface UploadResponse {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

export default function ContentUploadPage() {
  const router = useRouter();
  const [uploadedFile, setUploadedFile] = useState<UploadResponse | null>(null);

  const handleUploadSuccess = (response: UploadResponse) => {
    setUploadedFile(response);
  };

  const handleNext = () => {
    if (!uploadedFile) return;

    const queryParams = new URLSearchParams({
      videoId: uploadedFile.id.toString(),
      uploadUrl: uploadedFile.uploadUrl,
      originalFileName: uploadedFile.originalFileName
    }).toString();

    router.push(`/content/new/edit?${queryParams}`);
  };

  return (
      <div className="w-full mx-auto p-8">
        <h1 className="text-2xl text-black font-bold mb-6">컨텐츠 업로드</h1>
        {/* 스텝 네비게이션 */}
        <div className="w-full p-8 items-center">
          <div className="w-full grid grid-cols-3 mb-6">
            <div className="flex items-center font-semibold text-lg">
              <span className="text-[#4285f4] mr-2">1.</span>
              <span className="text-[#4285f4]">동영상 업로드</span>
            </div>
            <div className="flex items-center justify-center font-semibold text-lg">
              <span className="text-[#c5ccde] mr-2">2.</span>
              <span className="text-[#c5ccde]">정보 입력</span>
            </div>
            <div></div>
          </div>

          {/* 업로드 컴포넌트 */}
          <VideoUploader onUploadSuccess={handleUploadSuccess}/>


        {/* 하단 네비게이션 */}
        <div className="flex justify-end mt-8">
          <button
              onClick={handleNext}
              disabled={!uploadedFile}
              className={`
            px-4 py-2 rounded-full font-semibold text-sm
            ${uploadedFile
                  ? 'bg-[#4285f4] text-white hover:bg-[#4285f4]/90'
                  : 'bg-gray-200 text-gray-400 cursor-not-allowed'
              }
          `}
          >
            다음
          </button>
        </div>
        </div>
      </div>
  );
}