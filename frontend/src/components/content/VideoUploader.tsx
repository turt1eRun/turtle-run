// components/VideoUploader.tsx
'use client';

import { FC } from 'react';
import React, { useState, useRef } from 'react';
import { UploadCloud } from 'lucide-react';
import { uploadVideo } from "@/lib/api/content";

interface UploadResponse {
  id: number;
  uploadUrl: string;
  originalFileName: string;
}

interface VideoUploaderProps {
  onUploadSuccess: (response: UploadResponse) => void;
}

const VideoUploader: FC<VideoUploaderProps> = ({ onUploadSuccess }) => {
  const [dragActive, setDragActive] = useState(false);
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [uploadedFile, setUploadedFile] = useState<UploadResponse | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);

  const handleFileUpload = async (file: File) => {
    setError(null);
    setUploading(true);

    try {
      const response = await uploadVideo(file);
      setUploadedFile(response);
      onUploadSuccess(response);
    } catch (error) {
      setError('파일 업로드에 실패했습니다.')
      console.log(error);
    } finally {
      setUploading(false);
    }
  };

  const handleDrop = async (e: React.DragEvent) => {
    e.preventDefault();
    setDragActive(false);

    const file = e.dataTransfer.files[0];
    if(file) {
      await handleFileUpload(file);
    }
  };

  const handleFileSelect = async (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      await handleFileUpload(file);
    }
  };

  return (
      <div className="w-full p-8 bg-white rounded-lg shadow-sm border border-gray-500/[0.11]">
        <div
            className={`
          w-full
          border-2 border-dashed border-gray-300 
          rounded-lg 
          min-h-[300px]
          flex flex-col items-center justify-center
          ${dragActive ? 'bg-blue-50 border-blue-500' : 'bg-white'}
          ${uploading ? 'opacity-50' : ''}
          transition-colors
        `}
            onDragEnter={(e) => {
              e.preventDefault();
              setDragActive(true);
            }}
            onDragLeave={(e) => {
              e.preventDefault();
              setDragActive(false);
            }}
            onDragOver={(e) => e.preventDefault()}
            onDrop={handleDrop}
        >
          {uploading ? (
              <div className="flex flex-col items-center">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-600 mb-4"/>
                <p className="text-gray-600">업로드 중...</p>
              </div>
          ) : uploadedFile ? (
              <div className="flex flex-col items-center p-4">
                <div className="flex items-center justify-center mb-4">
                  <UploadCloud className="w-8 h-8 text-green-500 mr-2"/>
                  <span className="text-gray-500 font-semibold">업로드 완료!</span>
                </div>
                <p className="text-gray-700 font-medium">파일명: {uploadedFile.originalFileName}</p>
                <div className="mt-4 flex items-center gap-2 text-gray-500 text-sm">
                  <p>다른 파일을 드래그하거나</p>
                  <button
                      onClick={() => fileInputRef.current?.click()}
                      className="text-blue-500 hover:text-blue-600 font-medium"
                  >
                    파일 선택
                  </button>
                </div>
              </div>
          ) : (
              <>
                <UploadCloud className="w-14 h-14 text-[#6b7280] mb-4"/>
                <p className="text-gray-500 text-lg font-semibold">동영상 파일을 드래그 앤 드롭하여 업로드</p>
                <p className="text-gray-400 text-base mt-2">MPEG 또는 MP4 형식</p>
                <button
                    onClick={() => {
                      fileInputRef.current?.click();
                      setError(null);
                    }}
                    className="mt-6 px-4 py-2 bg-gray-600 text-white rounded-full font-semibold text-sm hover:bg-gray-700"
                >
                  파일 선택
                </button>
              </>
          )}
        </div>
        {error && (
            <div className="flex flex-col items-center mt-4">
              <p className="text-red-500 text-sm">{error}</p>
              <button
                  onClick={() => {
                    fileInputRef.current?.click();
                    setError(null);
                  }}
                  className="mt-2 px-4 py-2 bg-gray-200 text-gray-700 rounded-full text-sm hover:bg-gray-300"
              >
                다른 파일 선택
              </button>
            </div>
        )}
        <input
            type="file"
            ref={fileInputRef}
            className="hidden"
            accept="video/mp4,video/mpeg"
            onChange={handleFileSelect}
        />
      </div>
  );
};

export default VideoUploader;